package com.example.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.base.R
import com.example.base.onClick
import kotlinx.android.synthetic.main.view_bet_multiple_selector.view.*

class BetMultipleSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mMultipleValue: Int = 1
    private var mListener: OnMultipleValueChangeListener? = null

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
        clDecrease.onClick {
            if(mMultipleValue>1){
                mMultipleValue--
                updateDisplayValue()
            }
        }
        clIncrease.onClick {
            mMultipleValue++
            updateDisplayValue()}
    }

    private fun updateDisplayValue(){
        tvMultiple.text = mMultipleValue.toString()
        mListener?.onChange(mMultipleValue)
    }

    interface OnMultipleValueChangeListener{
        fun onChange(value: Int)
    }

    fun setOnMultipleValueChangeListener(listener: OnMultipleValueChangeListener){
        mListener = listener
    }

    fun getMultipleValue() = mMultipleValue
}