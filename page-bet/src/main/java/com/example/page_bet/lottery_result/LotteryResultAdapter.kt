package com.example.page_bet.lottery_result

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.text.Layout
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.constant.GameTypeId
import com.example.repository.model.bet.HistoricalResponse
import com.example.repository.model.bet.MultipleHistoryRecord


class LotteryResultAdapter(data: MutableList<MultipleHistoryRecord>, context: Context):
    BaseQuickAdapter<MultipleHistoryRecord, BaseViewHolder>(R.layout.item_lottery_result, data) {

    private lateinit var context: Context
    init {
        this.context = context
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleHistoryRecord?) {
        helper.let {
            var issueNumStr = String.format("第%s期", item?.data?.issueNum)
            it?.setText(R.id.tvResultIssueNum, issueNumStr)
            var itemType = item?.type
            it?.getView<LinearLayout>(R.id.llIssueResult)?.let { it ->
                if(null != itemType) {
                    setWinNum(it, itemType, item?.data)
                }
            }
        }
    }

    fun setWinNum(llResultView: LinearLayout, type: Int, data: HistoricalResponse.Data?) {
        var winNum = data?.winNum?.split(",")
        llResultView.removeAllViews()
        var luckAddtion = 0
        loop@for((index, it) in winNum?.withIndex()!!) {
            if(it.equals("")) return@loop
            when(type) {
                GameTypeId.RACING.typeId -> addNum(llResultView, it)
                GameTypeId.CHOOSE.typeId -> addNum(llResultView, it)
                GameTypeId.HURRY_THREE.typeId -> addNum(llResultView, it)
                GameTypeId.TIME_LOTTERY.typeId -> addNum(llResultView, it)
                GameTypeId.MARX_SIX.typeId -> {
                    if(index == winNum.size-1) {
                        addSign(llResultView, "+")
                    }
                    addNum(llResultView, it)
                }
                GameTypeId.LUCKY.typeId -> {
                    luckAddtion += it.toInt()
                    if(index>0) {
                        addSign(llResultView, "+")
                    }
                    addNum(llResultView, it)
                    if(index == winNum.size-1) {
                        addSign(llResultView, "=")
                        addNum(llResultView, luckAddtion.toString())
                    }
                }
                else -> {
                }
            }
        }
    }

    fun dpToPx(dp: Int): Int {
        var pxs: Int = (dp*Resources.getSystem().displayMetrics.density).toInt()
        return pxs
    }

    fun addSign(linearLayout: LinearLayout, sign:String) {
        var tvAdd = TextView(context)
        var parameter: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        parameter.gravity = Gravity.CENTER_VERTICAL
        parameter.setMargins(dpToPx(6), 0, 0, 0)
        tvAdd.layoutParams = parameter
        tvAdd.text = sign
        tvAdd.textSize = 14f
        tvAdd.gravity = Gravity.CENTER
        tvAdd.setTextColor(Color.parseColor("#000000"))
        linearLayout.addView(tvAdd)
    }

    fun addNum(linearLayout: LinearLayout, num:String) {
        var tvWinNum = TextView(context)
        var parameter : LinearLayout.LayoutParams = LinearLayout.LayoutParams(dpToPx(26), dpToPx(26))
        parameter.setMargins(dpToPx(6),0,0,0)
        tvWinNum.text = num
        tvWinNum.textSize = 14f
        tvWinNum.gravity = Gravity.CENTER
        tvWinNum.setTextColor(Color.parseColor("#000000"))
        tvWinNum.setBackgroundResource(R.drawable.bg_gray_circle_button)
        parameter.gravity = Gravity.CENTER_VERTICAL
        tvWinNum.layoutParams = parameter
        linearLayout.addView(tvWinNum)
    }

}