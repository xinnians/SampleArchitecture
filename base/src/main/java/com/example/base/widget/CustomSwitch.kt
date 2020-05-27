package com.example.base.widget

import android.content.Context
import android.graphics.Color
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.base.R
import com.example.base.onClick
import com.example.base.visible
import kotlinx.android.synthetic.main.custom_switch.view.*

class CustomSwitch  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var listener:OnSwitchCall? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_switch, this, false)
        val set = ConstraintSet()
        addView(view)
        set.clone(this)
        set.applyTo(this)

        val applyGameType = ConstraintSet()
        val resetGameType = ConstraintSet()
        applyGameType.clone(clSwitch1)
        resetGameType.clone(clSwitch1)

        var gameType = true
        tvLeft1.onClick {
            if (!gameType) {
                TransitionManager.beginDelayedTransition(clSwitch1)
                resetGameType.applyTo(clSwitch1)
                tvLeft1.setTextColor(Color.parseColor("#ffffff"))
                tvRight1.setTextColor(Color.parseColor("#adadad"))
                gameType = true
                listener?.onCall(gameType)
            }
        }

        tvRight1.onClick {
            if (gameType) {
                TransitionManager.beginDelayedTransition(clSwitch1)
                setView(applyGameType, R.id.view1, R.id.clSwitch1).applyTo(clSwitch1)
                tvRight1.setTextColor(Color.parseColor("#ffffff"))
                tvLeft1.setTextColor(Color.parseColor("#adadad"))
                gameType = false
                listener?.onCall(gameType)
            }
        }

        var gameRate = true
        val applyGameRate = ConstraintSet()
        val resetGameRate = ConstraintSet()
        applyGameRate.clone(clSwitch2)
        resetGameRate.clone(clSwitch2)
        tvLeft2.onClick {
            if (!gameRate) {
                TransitionManager.beginDelayedTransition(clSwitch2)
                resetGameRate.applyTo(clSwitch2)
                tvLeft2.setTextColor(Color.parseColor("#ffffff"))
                tvRight2.setTextColor(Color.parseColor("#000000"))
                gameRate = true
                listener?.onCall(gameRate)
            }
        }

        tvRight2.onClick {
            if (gameRate) {
                TransitionManager.beginDelayedTransition(clSwitch2)
                setView(applyGameRate, R.id.view2, R.id.clSwitch2).applyTo(clSwitch2)
                tvRight2.setTextColor(Color.parseColor("#ffffff"))
                tvLeft2.setTextColor(Color.parseColor("#000000"))
                gameRate = false
                listener?.onCall(gameRate)
            }
        }
    }

    private fun setView(set: ConstraintSet, resId: Int, viewId: Int): ConstraintSet {
        return set.apply {
            clear(resId, ConstraintSet.TOP)
            clear(resId, ConstraintSet.START)
            clear(resId, ConstraintSet.BOTTOM)
            connect(resId, ConstraintSet.END, viewId, ConstraintSet.END)
            connect(resId, ConstraintSet.TOP, viewId, ConstraintSet.TOP)
            connect(resId, ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM)
        }
    }

    companion object {
        const val GAME_TYPE = 1
        const val GAME_RATE = 2
    }

    var switchType: Int
        get() = this.switchType
        set(type) {
            when (type) {
                GAME_TYPE -> {
                    clSwitch1.visible()
                }

                GAME_RATE -> {
                    clSwitch2.visible()
                }
            }
        }

    interface OnSwitchCall{
        fun onCall(type: Boolean)
    }

    fun setOnSwitchCallListener(listener: OnSwitchCall){
        this.listener = listener
    }

}
