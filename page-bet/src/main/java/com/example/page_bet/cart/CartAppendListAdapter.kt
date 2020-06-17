package com.example.page_bet.cart

import android.graphics.Color
import android.widget.CheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.base.gone
import com.example.base.onClick
import com.example.base.visible
import com.example.page_bet.R
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout


class CartAppendListAdapter(data: MutableList<CartFragment.Append>,
                            private val callback: CartAppendDialog.SetCallback) :
    BaseQuickAdapter<CartFragment.Append, BaseViewHolder>(R.layout.item_append_layout, data) {
    private var isShowBox = false

    override fun convert(helper: BaseViewHolder, item: CartFragment.Append) {
        helper.setText(R.id.tvMoreIssueNo, item.appendIssueNo.toString())
        helper.setText(R.id.tvAmount, item.amount.toString())
        val cbAppend = helper.getView<CheckBox>(R.id.cbAppend)
        val clContentView = helper.getView<ConstraintLayout>(R.id.clAppendContentView)
        val esLayout = helper.getView<EasySwipeMenuLayout>(R.id.esLayout)
        val del = helper.getView<ConstraintLayout>(R.id.clDelAppend)
        if (isShowBox) {
            cbAppend.visible()
        } else {
            cbAppend.gone()
        }

        del.onClick {
            val deleteDialog = CartAppendDialog(mContext, item, helper.layoutPosition, esLayout)
            deleteDialog.setCallback(callback)
            deleteDialog.show()
        }

        cbAppend.setOnCheckedChangeListener { _, isChecked ->
            item.isCheck = isChecked
            clContentView.setBackgroundColor(Color.parseColor(if(isChecked) "#9b9b9b" else "#f8f8f8"))
        }

        clContentView.setBackgroundColor(Color.parseColor(if(item.isCheck) "#9b9b9b" else "#f8f8f8"))
    }

    fun setShowBox() {
        isShowBox = !isShowBox
    }

}