package com.example.page_bet.cart

import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.base.onClick
import com.example.page_bet.R
import com.example.repository.room.Cart
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout


class CartListAdapter(data: MutableList<Cart>, private val callback: CartDeleteDialog.SetCallback) :
    BaseQuickAdapter<Cart, BaseViewHolder>(R.layout.item_cart_layout, data) {

    override fun convert(helper: BaseViewHolder, item: Cart) {
        helper.setText(R.id.tvBetNumber, item.betNumber)
        helper.setText(R.id.tvAmount, item.amount.toString())
        helper.setText(R.id.tvBetCount, item.betCount.toString())
        val esLayout = helper.getView<EasySwipeMenuLayout>(R.id.esLayout)
        val del = helper.getView<ConstraintLayout>(R.id.clDel)
        del.onClick {
            val deleteDialog = CartDeleteDialog(mContext, item, esLayout)
            deleteDialog.setCallback(callback)
            deleteDialog.show()
        }
    }
}