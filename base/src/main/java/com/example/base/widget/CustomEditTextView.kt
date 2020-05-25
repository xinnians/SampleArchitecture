package com.example.base.widget

import android.content.Context
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.base.R
import com.example.base.gone
import com.example.base.onClick
import com.example.base.visible
import kotlinx.android.synthetic.main.custome_edittext.view.*

class CustomEditTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.custome_edittext, this,false)
        val set = ConstraintSet()
        addView(view)
        set.clone(this)
        set.applyTo(this)

        var isHide = true
        tvHide.onClick {
            if (isHide) {
                isHide = false
                tvHide.text = "hide"
                etInput.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                isHide = true
                tvHide.text = "show"
                etInput.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    object InType{
        const val NUMBER = InputType.TYPE_CLASS_NUMBER
        const val PASSWORD = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    var inputType: Int
    get() = etInput.inputType
    set(inputType) {
        etInput.inputType = inputType
    }

    var title: String
        get() = tvTitle.text.toString()
        set(title) {
            if (title.isNotBlank() || title.isNotEmpty()) {
                tvTitle.let {
                    it.visible()
                    it.text = title
                }
            } else {
                tvTitle.gone()
            }
        }

    var text: String
        get() = etInput.text.toString()
        set(text) {
            if (text.isNotBlank() || text.isNotEmpty()) {
                etInput.setText(text)
            }
        }


    var hint: String
        get() = this.toString()
        set(hint) {
            if (hint.isNotBlank() || hint.isNotEmpty()) {
                etInput.hint = hint
            }
        }

    var textVisible: Boolean
        get() = true
        set(textVisible) {
            if (textVisible) {
                tvHide.visible()
            } else {
                tvHide.gone()
            }
        }


    var notice: String
        get() = this.toString()
        set(notice) {
            if (notice.isNotBlank() || notice.isNotEmpty()) {
                tvNotice.let {
                    it.visible()
                    it.text = notice
                }
            } else {
                tvNotice.gone()
            }
        }

    fun setBackground(resId: Int) = etInput.setBackgroundResource(resId)
    fun textChangedListener(watch: TextWatcher) = etInput.addTextChangedListener(watch)



}