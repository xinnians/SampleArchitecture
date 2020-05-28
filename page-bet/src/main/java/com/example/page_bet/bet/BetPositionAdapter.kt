package com.example.page_bet.bet

import android.util.Log
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.bet.MultiplePlayTypePositionItem

class BetPositionAdapter(data: List<MultiplePlayTypePositionItem>) : BaseMultiItemQuickAdapter<MultiplePlayTypePositionItem, BaseViewHolder>(data) {

    init {
        //單式不需顯示選項，僅需顯示說明
        addItemType(0, R.layout.item_play_type_position_zero)
        //只有一個選項的時候
        addItemType(1, R.layout.item_play_type_position_one)
        addItemType(2, R.layout.item_play_type_position_two)
        addItemType(3, R.layout.item_play_type_position_three)
        addItemType(4, R.layout.item_play_type_position_four)
        addItemType(5, R.layout.item_play_type_position_five)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiplePlayTypePositionItem?) {
        Log.e("Ian", "[BetPositionAdapter] convert: $item, itemType: ${helper?.itemViewType}")

        helper?.let {
            it.setText(R.id.tvPosition, item?.getData()?.displayTitle)
            it.addOnClickListener(R.id.tvPosition)

            if(item?.getData()?.isSelect == true){
                it.setBackgroundRes(R.id.tvPosition,R.drawable.bg_white_25_corner_dark_stroke)
            }else{
                it.setBackgroundRes(R.id.tvPosition,R.drawable.bg_gray_25_corner)
            }
//            when (it.itemViewType) {
//                1 -> {
//
//                }
//                2 -> {
//
//                }
//                3 -> {
//
//                }
//                4 -> {
//
//                }
//                5 -> {
//
//                }
//                else -> {
//
//                }
//            }
        }
    }
}