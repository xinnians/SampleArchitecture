package com.example.page_bet.cart

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.room.Cart

class CartListAdapter(data: MutableList<Cart>) :
    BaseQuickAdapter<Cart, BaseViewHolder>(R.layout.item_cart_layout, data) {

    override fun convert(helper: BaseViewHolder, item: Cart) {
        helper.setText(R.id.tvBetNumber, item.betNumber)
        helper.setText(R.id.tvAmount, item.amount.toString())
        helper.setText(R.id.tvBetCount, item.betCount.toString())
    }
}