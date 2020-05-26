package com.example.page_bet.bet

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.onClick
import com.example.page_bet.R
import com.example.repository.model.BetGroupEntity
import com.example.repository.model.BetTypeEntity
import com.example.repository.model.PlayTypeInfoEntity
import kotlinx.android.synthetic.main.dialog_play_type.*

class PlayTypeDialog(context: Context, var data: List<BetTypeEntity>, listener: OnPlayTypeSelectListener? = null) : Dialog(context) {

    private var mBetTypeAdapter: BetTypeAdapter? = null
    private var mBetGroupAdapter: BetGroupAdapter? = null
    private var mOnPlayTypeSelectListener: OnPlayTypeSelectListener? = listener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_play_type)
        window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }

        init()
        initBetTypeView()
        initBetGroupView(null)
    }

    private fun init(){
        tvBottomButton.onClick { close() }
    }

    private fun initBetTypeView() {
        var layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvBetType.layoutManager = layoutManager

        mBetTypeAdapter = BetTypeAdapter(data)
        mBetTypeAdapter?.setOnItemChildClickListener { adapter, view, position ->
            Log.e("Ian", "[mBetTypeAdapter] onclick position:$position")
            for (item in data) item.isSelect = false
            data[position].isSelect = true
            adapter.notifyDataSetChanged()

            setBetGroupData(data[position].mobileBetGroupEntityList)
        }

        rvBetType.adapter = mBetTypeAdapter
    }

    private fun initBetGroupView(betGroupEntityList: List<BetGroupEntity>?) {
        var layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvBetGroup.layoutManager = layoutManager

        mBetGroupAdapter = BetGroupAdapter(betGroupEntityList ?: listOf())
        mBetGroupAdapter?.setOnPlayTypeSelectListener(object : BetGroupAdapter.OnPlayTypeSelect{
            override fun onSelect(playTypeCode: Int, playTypeDisplayName: String) {
                mOnPlayTypeSelectListener?.onSelect(playTypeCode,playTypeDisplayName,"","")
                close()
            }
        })
        rvBetGroup.adapter = mBetGroupAdapter
    }

    private fun setBetGroupData(betGroupEntityList: List<BetGroupEntity>?){
        mBetGroupAdapter?.let {
            it.setNewData(betGroupEntityList)
            it.notifyDataSetChanged()
        }
    }

    interface OnPlayTypeSelectListener{
        fun onSelect(playTypeCode: Int, playTypeName: String, betGroupName: String, betTypeName: String)
    }

    private fun close(){
        this.dismiss()
    }
}