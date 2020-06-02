package com.example.page_bet.bet

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.constant.BetUnitDisplayMode
import com.example.repository.model.bet.MultipleBetUnit

class BetUnitAdapter(data: List<MultipleBetUnit>,var parentPostion: Int) :
    BaseMultiItemQuickAdapter<MultipleBetUnit, BaseViewHolder>(data) {

    init {
        addItemType(BetUnitDisplayMode.ONLY_NUMBER.typeNumber, R.layout.item_bet_unit_only_number)
        addItemType(BetUnitDisplayMode.ONE_CHAR.typeNumber, R.layout.item_bet_unit_one_char)
        addItemType(BetUnitDisplayMode.TWO_CHAR.typeNumber, R.layout.item_bet_unit_two_char)
        addItemType(BetUnitDisplayMode.THREE_CHAR.typeNumber, R.layout.item_bet_unit_three_char)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleBetUnit?) {
        helper?.let {
            if(item?.data?.isSelect == true){
                it.setBackgroundRes(R.id.tvUnitTitle,R.drawable.bg_darkgray_circle)
                mContext?.getColor(R.color.colorWhite)?.let { it1 ->
                    it.setTextColor(R.id.tvUnitTitle,
                        it1
                    )
                }
            }else{
                it.setBackgroundRes(R.id.tvUnitTitle,R.drawable.bg_white_circle_gray_stroke)
                mContext?.getColor(R.color.colorLittleBlack)?.let { it1 ->
                    it.setTextColor(R.id.tvUnitTitle,
                        it1
                    )
                }
            }


            it.setText(R.id.tvUnitTitle, item?.data?.unitName)
            it.addOnClickListener(R.id.tvUnitTitle)
        }
    }
}