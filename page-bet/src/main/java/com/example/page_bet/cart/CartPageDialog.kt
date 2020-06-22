package com.example.page_bet.cart

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.example.base.gone
import com.example.base.onClick
import com.example.base.visible
import com.example.base.widget.CustomSwitch
import com.example.page_bet.R
import com.example.repository.room.Cart
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout
import kotlinx.android.synthetic.main.dialog_cart_page.*
import kotlinx.android.synthetic.main.dialog_cart_page.csPlayRate
import kotlinx.android.synthetic.main.fragment_bet.*

class CartPageDialog(context: Context,private val type:Int, private val view: View,
                     private val cart: Cart, private val position: Int,
                     private val esLayout: EasySwipeMenuLayout): BottomSheetDialog(context) {

    companion object {
        const val DEL = 1
        const val EDIT = 2
        const val APPEND = 3
        const val MORE_TYPE_1 = 1
        const val MORE_TYPE_2 = 2
        const val MORE_TYPE_3 = 3
    }
    private var appendType = -1
    private var isWinStop = false
    private var isMoreWinStop = false
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

        csPlayRate.switchType = CustomSwitch.GAME_RATE
        rbWinStop.onClick {
            if (!isWinStop) {
                isWinStop = true
                rbWinStop.isChecked = isWinStop
            } else {
                isWinStop = false
                rbWinStop.isChecked = isWinStop
            }
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

                btnMore.onClick {
                    tvCartDialogTitle.text = "更多"
                    clAppendLayout.gone()
                    clAppendMoreLayout.visible()
                    setAppendMoreListener()
                }

                rbMoreWinStop.onClick {
                    if (!isMoreWinStop) {
                        isMoreWinStop = true
                        rbMoreWinStop.isChecked = isMoreWinStop
                    } else {
                        isMoreWinStop = false
                        rbMoreWinStop.isChecked = isMoreWinStop
                    }
                }

                btnGenerate.onClick {
                    val setting = JsonObject()

                    setting.apply {

//                        addProperty("appendCount", etMoreCount.text.toString().toInt())
                        addProperty("appendCount", 20)
                        addProperty("isWinStop", rbMoreWinStop.isChecked)
//                        addProperty("multiple", etAppendMultiple.text.toString().toInt())
                        addProperty("multiple", 20)
                    }

                    when (appendType) {
                        MORE_TYPE_1 -> {
                            setting.addProperty("type", appendType)
                        }

                        MORE_TYPE_2 -> {
                            setting.addProperty("type", appendType)
                        }

                        MORE_TYPE_3 -> {
                            setting.addProperty("type", appendType)
                        }
                    }

                    listener?.onAppendCall(view, cart, position, setting)
                    esLayout.resetStatus()
                    dismiss()
                }
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

    private fun setAppendMoreListener() {
        tvAppendType1.setOnClickListener(clickListener)
        tvAppendType2.setOnClickListener(clickListener)
        tvAppendType3.setOnClickListener(clickListener)
        changeLayout(MORE_TYPE_1)
    }

    private val clickListener = View.OnClickListener { view ->
        when (view.id) {
            tvAppendType1.id -> {
                changeLayout(MORE_TYPE_1)
            }
            tvAppendType2.id -> {
                changeLayout(MORE_TYPE_2)
            }
            tvAppendType3.id -> {
                changeLayout(MORE_TYPE_3)
            }
        }
    }

    private fun changeLayout(type: Int) {
        when (type) {
            MORE_TYPE_1 -> {
                clContentRow2.gone()
                clContentRow3.gone()
                appendType = MORE_TYPE_1
            }

            MORE_TYPE_2 -> {
                clContentRow2.visible()
                clContentRow3.gone()
                appendType = MORE_TYPE_2
            }

            MORE_TYPE_3 -> {
                clContentRow2.gone()
                clContentRow3.visible()
                appendType = MORE_TYPE_3
            }
        }
        changeBackground(type)
    }

    private fun changeBackground(type: Int){
        tvAppendType1.setBackgroundResource(if(type == MORE_TYPE_1) R.drawable.bg_lightgray_5_corner else R.drawable.bg_white_5_corner)
        tvAppendType2.setBackgroundResource(if(type == MORE_TYPE_2) R.drawable.bg_lightgray_5_corner else R.drawable.bg_white_5_corner)
        tvAppendType3.setBackgroundResource(if(type == MORE_TYPE_3) R.drawable.bg_lightgray_5_corner else R.drawable.bg_white_5_corner)
    }

    interface SetCallback{
        fun onCall(view: View, cart: Cart, position: Int)
        fun onAppendCall(view: View, cart: Cart, position: Int, setting: JsonObject)
    }

    fun setCallback(callback: SetCallback){
        this.listener = callback
    }
}
