package com.example.page_bet.cart

import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.base.gone
import com.example.base.onClick
import com.example.base.visible
import com.example.page_bet.R
import com.example.repository.room.Cart
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout


class CartListAdapter(data: MutableList<Cart>,
                      private val callback: CartPageDialog.SetCallback) :
    BaseQuickAdapter<Cart, BaseViewHolder>(R.layout.item_cart_layout, data) {

    override fun convert(helper: BaseViewHolder, item: Cart) {
        helper.setText(R.id.tvBetNumber, item.betNumber)
        helper.setText(R.id.tvAmount, item.amount.toString())
        helper.setText(R.id.tvBetCount, item.betCount.toString())
        val clContentView = helper.getView<ConstraintLayout>(R.id.clContentView)
        val appendLogo = helper.getView<ConstraintLayout>(R.id.clAppendLogo)
        if (item.isAppend) {
            appendLogo.visible()
            clContentView.setBackgroundColor(Color.parseColor("#9b9b9b"))
        } else {
            appendLogo.gone()
            clContentView.setBackgroundColor(Color.parseColor("#ffffff"))
        }

        val esLayout = helper.getView<EasySwipeMenuLayout>(R.id.esLayout)
        val del = helper.getView<ConstraintLayout>(R.id.clDelCart)
        del.onClick {
            val deleteDialog = CartPageDialog(mContext, CartPageDialog.DEL, del, item,
                                              helper.layoutPosition, esLayout)
            deleteDialog.setCallback(callback)
            deleteDialog.show()
        }

        val edit = helper.getView<ConstraintLayout>(R.id.clEditCart)
        edit.onClick {
            val editDialog = CartPageDialog(mContext, CartPageDialog.EDIT, edit, item,
                                            helper.layoutPosition, esLayout)
            editDialog.setCallback(callback)
            editDialog.show()
        }

        val append = helper.getView<ConstraintLayout>(R.id.clAppendCart)
        append.onClick {
            val appendDialog = CartPageDialog(mContext, CartPageDialog.APPEND, append, item,
                                              helper.layoutPosition, esLayout)
            appendDialog.setCallback(callback)
            appendDialog.show()
        }
    }
}