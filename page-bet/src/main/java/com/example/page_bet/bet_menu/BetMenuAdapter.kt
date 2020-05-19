package com.example.page_bet.bet_menu

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.GameMenuResponse

class BetMenuAdapter(data: MutableList<MultipleMenuItem>) :
    BaseMultiItemQuickAdapter<MultipleMenuItem, BaseViewHolder>(data) {
    init {
        addItemType(MultipleMenuItem.HOT, R.layout.item_hot)
        addItemType(MultipleMenuItem.FAVORITE, R.layout.item_favorite)
        addItemType(MultipleMenuItem.NORMAL, R.layout.item_normal)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleMenuItem?) {
        helper?.let {
            helper.setText(R.id.tvTypeName,item?.getData()?.gameTypeDisplayName?:"empty")
            when (it.itemViewType) {
                MultipleMenuItem.HOT -> {

                }
                MultipleMenuItem.FAVORITE -> {

                }
                MultipleMenuItem.NORMAL -> {

                }
                else -> {

                }
            }
        }
    }
}