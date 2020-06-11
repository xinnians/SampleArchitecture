package com.example.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.base.R
import kotlinx.android.synthetic.main.view_bet_unit_selector.view.*

class BetUnitSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var mUnitSelectListener: OnUnitSelectListener? = null
    private var mCurrentBetCurrency: Currency = Currency.One
    private var mCurrentBetUnit: Unit = Unit.Dollar

    enum class Unit(var value: Double) {
        Dollar(1.0),
        Dime(0.1),
        Penny(0.01),
        Cent(0.001)
    }

    enum class Currency(var value: Int) {
        One(1),
        Two(2)
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_bet_unit_selector,this,false)
        // init ConstraintLayout custom view
        val set = ConstraintSet()
        addView(view)
        set.clone(this)
        set.applyTo(this)
        initViewListener()
    }

    private fun initViewListener(){
        tvDollar.setOnClickListener(this)
        tvDime.setOnClickListener(this)
        tvPenny.setOnClickListener(this)
        tvCent.setOnClickListener(this)
        ivSwitch.setOnClickListener(this)
    }

    interface OnUnitSelectListener{
        fun onSelect(unitValue: Double,currency: Int)
    }

    fun addOnUnitSelectListener(listener: OnUnitSelectListener){
        mUnitSelectListener = listener
    }

    override fun onClick(v: View?) {
        when(v?.id){
            tvDollar.id -> {
                mCurrentBetUnit = Unit.Dollar
                changeUnitDisplay(mCurrentBetUnit)
            }
            tvDime.id -> {
                mCurrentBetUnit = Unit.Dime
                changeUnitDisplay(mCurrentBetUnit)
            }
            tvPenny.id -> {
                mCurrentBetUnit = Unit.Penny
                changeUnitDisplay(mCurrentBetUnit)
            }
            tvCent.id -> {
                mCurrentBetUnit = Unit.Cent
                changeUnitDisplay(mCurrentBetUnit)
            }
            ivSwitch.id -> {
                switchBetCurrency()
            }
        }
        mUnitSelectListener?.onSelect(mCurrentBetUnit.value,mCurrentBetCurrency.value)
    }

    private fun switchBetCurrency(){
        mCurrentBetCurrency = if(mCurrentBetCurrency == Currency.One) Currency.Two else Currency.One
        changeCurrencyDisplay(mCurrentBetCurrency)
    }

    private fun changeUnitDisplay(unit: Unit){
        tvDollar.setBackgroundResource(if(unit == Unit.Dollar) R.drawable.bg_gray_15_corner_dark_gray_stroke else R.drawable.bg_gray_15_corner)
        tvDime.setBackgroundResource(if(unit == Unit.Dime) R.drawable.bg_gray_15_corner_dark_gray_stroke else R.drawable.bg_gray_15_corner)
        tvPenny.setBackgroundResource(if(unit == Unit.Penny) R.drawable.bg_gray_15_corner_dark_gray_stroke else R.drawable.bg_gray_15_corner)
        tvCent.setBackgroundResource(if(unit == Unit.Cent) R.drawable.bg_gray_15_corner_dark_gray_stroke else R.drawable.bg_gray_15_corner)
    }

    private fun changeCurrencyDisplay(currency: Currency){
        when(currency){
            Currency.One -> {
                tvDollar.text = "1元"
                tvDime.text = "1角"
                tvPenny.text = "1分"
                tvCent.text = "1厘"
            }
            Currency.Two -> {
                tvDollar.text = "2元"
                tvDime.text = "2角"
                tvPenny.text = "2分"
                tvCent.text = "2厘"
            }
        }
    }

}