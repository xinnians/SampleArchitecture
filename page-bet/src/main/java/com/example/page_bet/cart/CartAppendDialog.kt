package com.example.page_bet.cart

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.base.gone
import com.example.base.onClick
import com.example.base.visible
import com.example.page_bet.R
import com.example.repository.room.Cart
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout
import kotlinx.android.synthetic.main.dialog_cart_page.*

class CartAppendDialog(context: Context, private val append: CartFragment.Append,
                       private val position: Int,
                       private val esLayout: EasySwipeMenuLayout): BottomSheetDialog(context) {

    private var listener: SetCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_cart_page)
        window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }

        tvCartDialogTitle.text = "确认删除此注单？"
        clDelAndEditLayout.visible()
        btnConfirm.visible()
        ivClose.onClick {
            dismiss()
        }

        btnConfirm.onClick {
            listener?.onCall(append, position)
            esLayout.resetStatus()
            dismiss()
        }
    }

    interface SetCallback{
        fun onCall(append: CartFragment.Append, position: Int)
    }

    fun setCallback(callback: SetCallback){
        this.listener = callback
    }
}
