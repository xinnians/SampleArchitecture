package com.example.page_bet.cart

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.example.base.observeNotNull
import com.example.base.onClick
import com.example.base.toast
import com.example.base.widget.CustomSwitch
import com.example.page_bet.R
import com.example.page_bet.bet.BetViewModel
import com.example.repository.model.base.ViewState
import com.example.repository.room.Cart
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout
import kotlinx.android.synthetic.main.dialog_cart_delete.*

class CartDeleteDialog(context: Context, private val cart: Cart, private val position: Int,
                       private val esLayout: EasySwipeMenuLayout): BottomSheetDialog(context) {
    private var listener: SetCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_cart_delete)
        window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }

        ivClose.onClick {
            dismiss()
        }

        btnDelConfirm.onClick {
            listener?.del(cart, position)
            esLayout.resetStatus()
            dismiss()
        }
    }

    interface SetCallback{
        fun del(cart: Cart, position: Int)
    }

    fun setCallback(callback: SetCallback){
        this.listener = callback
    }
}
