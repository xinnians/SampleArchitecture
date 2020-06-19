package com.example.page_transation

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class TransAdapter(data: MutableList<TransationFragment.FakeUserCashDetail>):
    BaseQuickAdapter<TransationFragment.FakeUserCashDetail,BaseViewHolder>(R.layout.item_transation, data) {

    override fun convert(helper: BaseViewHolder?, item: TransationFragment.FakeUserCashDetail?) {
        // TODO
        helper.let {
            it?.setText(R.id.tvItemGameType, item?.gameType)
            it?.setText(R.id.tvItemDate, item?.transDate)
            it?.setText(R.id.tvItemAmount, item?.transAmount.toString())
        }
    }
}