package com.example.page_bet.cart

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.base.onClick
import com.example.base.visible
import com.example.page_bet.R
import com.example.repository.room.Cart
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout
import kotlinx.android.synthetic.main.dialog_cart_page.*

class CartPageDialog(context: Context,private val type:Int, private val view: View,
                     private val cart: Cart, private val position: Int,
                     private val esLayout: EasySwipeMenuLayout): BottomSheetDialog(context) {

    companion object {
        const val DEL = 1
        const val EDIT = 2
        const val APPEND = 3
        const val MORE = 4
    }
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

        when (type) {
            DEL -> {
                tvCartDialogTitle.text = "确认删除此注单？"
                clDelAndEditLayout.visible()
                btnConfirm.visible()
            }

            EDIT -> {
                tvCartDialogTitle.text = "更改"
                clDelAndEditLayout.visible()
                clEditLayout.visible()
                btnConfirm.visible()

                cart.amount = 20000
                cart.betNumber = "1,2,3,3,3"
                cart.betUnit = 2.0
            }

            APPEND -> {
                tvCartDialogTitle.text = "追号"
                clAppendLayout.visible()
                btnAppend.onClick {
                    if (etAppendCount.text.isNotBlank() && etAppendCount.text.isNotEmpty()) {
                        cart.appendCount = etAppendCount.text.toString().toInt()
                        cart.isAppend = true
                        cart.isWinStop = rbWinStop.isChecked
                        listener?.onCall(view, cart, position)
                        esLayout.resetStatus()
                        dismiss()
                    } else {
                        return@onClick
                    }
                }
            }

            MORE -> {
                tvCartDialogTitle.text = "更多"
            }
        }

        ivClose.onClick {
            dismiss()
        }

        btnConfirm.onClick {
            listener?.onCall(view, cart, position)
            esLayout.resetStatus()
            dismiss()
        }
    }

    interface SetCallback{
        fun onCall(view: View, cart: Cart, position: Int)
    }

    fun setCallback(callback: SetCallback){
        this.listener = callback
    }
}
