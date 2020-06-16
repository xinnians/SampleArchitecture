package com.example.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.base.R

class BetMultipleSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_bet_multiple_selector, this, false)
        // init ConstraintLayout custom view
        val set = ConstraintSet()
        addView(view)
        set.clone(this)
        set.applyTo(this)
        initViewListener()
    }

    private fun initViewListener() {

    }
}