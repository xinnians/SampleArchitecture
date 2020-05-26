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

class CustomSwitch  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_switch, this, false)
        val set = ConstraintSet()
        addView(view)
        set.clone(this)
        set.applyTo(this)


    }



}
