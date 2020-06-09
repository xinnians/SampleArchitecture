package com.example.page_bet.lottery_result

import android.util.Log
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.bet.MultipleHistoryRecord
import com.example.repository.model.bet.MultipleIssueResultItem

class LotteryResultAdapter(data: MutableList<MultipleHistoryRecord>):
    BaseQuickAdapter<MultipleHistoryRecord, BaseViewHolder>(R.layout.item_lottery_result, data) {

    override fun convert(helper: BaseViewHolder?, item: MultipleHistoryRecord?) {
        helper.let {
//            Log.d("msg", "convert helper: ${item?.data?.issueNum}")
            it?.setText(R.id.tvResultIssueNum, item?.data?.issueNum)
            it?.setText(R.id.tvResultWinNum, item?.data?.winNum)
        }
    }

}