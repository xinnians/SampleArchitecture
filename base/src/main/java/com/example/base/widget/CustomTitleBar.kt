package com.example.base.widget

import android.content.Context
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.base.R
import com.example.base.*
import com.example.base.drawable
import com.example.base.onClick
import kotlinx.android.synthetic.main.custom_title_bar.view.*

class CustomTitleBar  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_title_bar, this, false)
        val set = ConstraintSet()
        addView(view)
        set.clone(this)
        set.applyTo(this)

        var isHide = true
        ivShowMoney.onClick {
            if (isHide) {
                isHide = false
                ivShowMoney.setImageDrawable(context.drawable(R.drawable.ic_icon_eyes_form_open))
//                amount.transformationMethod = HideReturnsTransformationMethod.getInstance()
                amount.text = money
                Handler().postDelayed({
                    ivShowMoney.setImageDrawable(context.drawable(R.drawable.ic_icon_eyes_form))
//                    amount.transformationMethod = PasswordTransformationMethod.getInstance()
                    amount.text = hideText
                    isHide = true
                }, showTime)
            } else {
                isHide = true
                ivShowMoney.setImageDrawable(context.drawable(R.drawable.close_eye))
                amount.let {
//                    it.transformationMethod = PasswordTransformationMethod.getInstance()
                    amount.text = hideText
                }
                Handler().removeCallbacksAndMessages(null)
            }
        }
    }

    var hideText: String = "・・・・・・・"

    var money: String
//        get() = this.toString()
        get() = "168,888,888.000"
        set(money){
            if (money.isNotBlank() || money.isNotEmpty()) {
                amount.text = money
            } else {
                amount.text = "0"
            }
//            amount.transformationMethod = PasswordTransformationMethod.getInstance()
        }

    var showTime: Long = 1000

    var showBack: Boolean
    get() = false
    set(showBack) {
        if (showBack) {
            ivBack.visible()
        } else {
            ivBack.gone()
        }
    }

    fun backListener(listener: OnClickListener) {
        ivBack.setOnClickListener(listener)
    }

    fun setBackButtonVisable(visable: Int){
        ivBack.visibility = visable
    }

}
