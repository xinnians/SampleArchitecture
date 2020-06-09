package com.example.page_bet.cart

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.bet.MultipleMenuItem

class CartAdapter(data: MutableList<MultipleMenuItem>) :
    BaseQuickAdapter<MultipleMenuItem, BaseViewHolder>(R.layout.item_cart_layout, data) {

    override fun convert(helper: BaseViewHolder?, item: MultipleMenuItem?) {
        TODO("Not yet implemented")
    }
}