package com.example.page_bet.bet

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.constant.BetUnitDisplayMode
import com.example.repository.model.bet.MultipleBetUnit

class BetUnitAdapter(data: List<MultipleBetUnit>) :
    BaseMultiItemQuickAdapter<MultipleBetUnit, BaseViewHolder>(data) {
    init {
        addItemType(BetUnitDisplayMode.ONLY_NUMBER.typeNumber, R.layout.item_bet_unit_only_number)
        addItemType(BetUnitDisplayMode.ONE_CHAR.typeNumber, R.layout.item_bet_unit_one_char)
        addItemType(BetUnitDisplayMode.TWO_CHAR.typeNumber, R.layout.item_bet_unit_two_char)
        addItemType(BetUnitDisplayMode.THREE_CHAR.typeNumber, R.layout.item_bet_unit_three_char)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleBetUnit?) {

        helper?.let {
            it.setText(R.id.tvUnitTitle,item?.data?.unitName)
        }
    }
}