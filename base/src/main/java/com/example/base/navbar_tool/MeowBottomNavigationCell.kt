package com.example.base.navbar_tool

import android.animation.*
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.example.base.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.meow_navigation_cell.view.*
import kotlin.math.max

@Suppress("unused")
class MeowBottomNavigationCell : RelativeLayout, LayoutContainer {

    companion object {
        const val EMPTY_VALUE = "empty"

        private val sExpandInterpolator: Interpolator = OvershootInterpolator()
        private val sCollapseInterpolator: Interpolator = DecelerateInterpolator(3f)
        private val sAlphaExpandInterpolator: Interpolator = DecelerateInterpolator()

        private val ANIMATION_DURATION = 300

        private val mExpandAnimation = AnimatorSet().setDuration(ANIMATION_DURATION.toLong())
        private val mCollapseAnimation = AnimatorSet().setDuration(ANIMATION_DURATION.toLong())
    }

    private var mViewCount: Int = 0
    private var mBaseBlockHeight: Int = 0
    private var mViewHeight: Int = 0
    private var mViewWidth: Int = 0
    private var mButtonSpacing: Int = 0
    private var mExpanded = false
    private var mSubItemList = arrayListOf<View>()

    var defaultIconColor = 0
        set(value) {
            field = value
            if (allowDraw)
                iv.color = if (!isEnabledCell) defaultIconColor else selectedIconColor
        }
    var selectedIconColor = 0
        set(value) {
            field = value
            if (allowDraw)
                iv.color = if (isEnabledCell) selectedIconColor else defaultIconColor
        }
    var circleColor = 0
        set(value) {
            field = value
            if (allowDraw)
                isEnabledCell = isEnabledCell
        }

    var icon = 0
        set(value) {
            field = value
            if (allowDraw)
                iv.resource = value
        }

    var count: String? = EMPTY_VALUE
        set(value) {
            field = value
            if (allowDraw) {
                if (count != null && count == EMPTY_VALUE) {
                    tv_count.text = ""
                    tv_count.visibility = View.INVISIBLE
                } else {
                    if (count != null && count?.length ?: 0 >= 3) {
                        field = count?.substring(0, 1) + ".."
                    }
                    tv_count.text = count
                    tv_count.visibility = View.VISIBLE
                    val scale = if (count?.isEmpty() == true) 0.5f else 1f
                    tv_count.scaleX = scale
                    tv_count.scaleY = scale
                }
            }
        }

    private var iconSize = dip(context, 48)
        set(value) {
            field = value
            if (allowDraw) {
                iv.size = value
                iv.pivotX = iconSize / 2f
                iv.pivotY = iconSize / 2f
            }
        }

    var countTextColor = 0
        set(value) {
            field = value
            if (allowDraw)
                tv_count.setTextColor(field)
        }

    var countBackgroundColor = 0
        set(value) {
            field = value
            if (allowDraw) {
                val d = GradientDrawable()
                d.setColor(field)
                d.shape = GradientDrawable.OVAL
                ViewCompat.setBackground(tv_count, d)
            }
        }

    var countTypeface: Typeface? = null
        set(value) {
            field = value
            if (allowDraw && field != null)
                tv_count.typeface = field
        }

    var rippleColor = 0
        set(value) {
            field = value
            if (allowDraw) {
                isEnabledCell = isEnabledCell
            }
        }

    var isFromLeft = false
    var duration = 0L

    //由該變數控制動畫進度
    private var progress = 0f
        set(value) {
            field = value
//            fl.y = (1f - progress) * dip(context, 6) + dip(context, -2)
            fl.y = (1f - progress) * mBaseBlockHeight * 0.2f + (mViewHeight - mBaseBlockHeight) + dip(context, -6)

            iv.color = if (progress == 1f) selectedIconColor else defaultIconColor
            val scale = (1f - progress) * (-0.2f) + 1f
            iv.scaleX = scale
            iv.scaleY = scale

            val d = GradientDrawable()
            d.setColor(circleColor)
//            d.shape = GradientDrawable.OVAL
            d.shape = GradientDrawable.RING
            ViewCompat.setBackground(v_circle, d)

            ViewCompat.setElevation(v_circle, if (progress > 0.7f) dipf(context, progress * 4f) else 0f)

            val m = dip(context, 24)
            v_circle.x = (1f - progress) * (if (isFromLeft) -m else m) + ((measuredWidth - dip(context, 48)) / 2f)
//            v_circle.y = (1f - progress) * measuredHeight + dip(context, 6)
//            Log.e("Ian","[progress] progress:$progress, mViewHeight:$mViewHeight, mBaseBlockHeight:$mBaseBlockHeight, iv.y:${iv.y}, dip(context, 3):${dip(context, 3)}")
            v_circle.y = (1f - progress) * measuredHeight + mViewHeight - mBaseBlockHeight + dip(context, 3)
        }

    private var subProgress = 0f
        set(value) {
            field = value
            var nextY = v_circle.y - mButtonSpacing
            for (i in 0 until mSubItemList.size) {

                val d = GradientDrawable()
                d.setColor(circleColor)
                /**
                 * 使用 OVAL 畫圓會造成圖片有陰影
                 */
//                d.shape = GradientDrawable.OVAL
                d.shape = GradientDrawable.RING
                ViewCompat.setBackground(mSubItemList[i], d)
                ViewCompat.setElevation(mSubItemList[i], if (subProgress > 0.7f) dipf(context, subProgress * 4f) else 0f)
                mSubItemList[i].y = (1f - subProgress) * measuredHeight + nextY - mSubItemList[i].measuredHeight
                nextY = nextY - mSubItemList[i].measuredHeight - mButtonSpacing
            }
        }

    var isEnabledCell = false
        set(value) {
            field = value
            val d = GradientDrawable()
            d.setColor(circleColor)
            d.shape = GradientDrawable.OVAL
            if (Build.VERSION.SDK_INT >= 21 && !isEnabledCell) {
//                fl.background = RippleDrawable(ColorStateList.valueOf(rippleColor), null, d)
                fl.runAfterDelay(200) {
                    fl.setBackgroundColor(Color.TRANSPARENT)
                }
            } else {
                fl.runAfterDelay(200) {
                    fl.setBackgroundColor(Color.TRANSPARENT)
                }
            }
        }

    var onClickListener: () -> Unit = {}
        set(value) {
            field = value
            iv?.setOnClickListener {
//                Log.e("Ian","iv OnClickListener")
                onClickListener()
//                if (right_labels.isExpanded) right_labels.collapse() else right_labels.expand()
            }
//            right_labels?.setOnClickListener {
//                Log.e("Ian","right_labels OnClickListener")
//                onClickListener()
//                if (right_labels.isExpanded) right_labels.collapse() else right_labels.expand()
//            }
        }

    override lateinit var containerView: View
    private var allowDraw = false

    constructor(context: Context) : super(context) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setAttributeFromXml(context, attrs)
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setAttributeFromXml(context, attrs)
        initializeView()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun setAttributeFromXml(context: Context, attrs: AttributeSet) {
    }

    private fun initializeView() {
        allowDraw = true
        containerView = LayoutInflater.from(context).inflate(R.layout.meow_navigation_cell, this)
        mButtonSpacing = dip(context, 10)
        draw()
    }

    private fun draw() {
        if (!allowDraw)
            return

        icon = icon
        count = count
        iconSize = iconSize
        countTextColor = countTextColor
        countBackgroundColor = countBackgroundColor
        countTypeface = countTypeface
        rippleColor = rippleColor
        onClickListener = onClickListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        progress = progress
        subProgress = subProgress

        var height = 0
        mViewWidth = 0

        measureChildren(widthMeasureSpec, heightMeasureSpec)

        for (i in 0..childCount) {
            var child = getChildAt(i)
//            try {
//                Log.e("Ian","[onLayout] index:$i, tag:${child.tag}, childCount:$childCount")
//            }catch (e: Exception){
//                Log.e("Ian","[onLayout] Exception:$e")
//            }

            child?.let {
                if (it.tag != "v_circle")
                    height += it.measuredHeight
                if (it.tag == "FrameLayout")
                    mBaseBlockHeight = it.height

                mViewWidth = max(mViewWidth, it.measuredWidth)
            }
        }

        mViewHeight = (height * 1.4f).toInt()

        setMeasuredDimension(widthMeasureSpec, mViewHeight)

    }

    fun disableCell() {
//        Log.d("msg", "disableCell isEnableCell: " + isEnabledCell)
        if (isEnabledCell)
            animateProgress(false)
        isEnabledCell = false
    }

    fun enableCell(isAnimate: Boolean = true) {
//        Log.d("msg", "enableCell isEnableCell: " + isEnabledCell)
        if (!isEnabledCell)
            animateProgress(true, isAnimate)
        isEnabledCell = true
    }

    private fun animateProgress(enableCell: Boolean, isAnimate: Boolean = true) {
        val d = if (enableCell) duration else 250
        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.apply {
            startDelay = if (enableCell) d / 4 else 0L
            duration = if (isAnimate) d else 1L
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                val f = it.animatedFraction
                progress = if (enableCell)
                    f
                else
                    1f - f
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    animateSubProgress(enableCell)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
//                    for(item in mSubItemList) {
//                        item.visibility = View.VISIBLE
//                    }
                }

            })
            start()
        }
    }

    private fun animateSubProgress(enableCell: Boolean) {
        val d = if (enableCell) duration else 250
        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.apply {
            startDelay = if (enableCell) d / 4 else 0L
            duration = d
            interpolator = FastOutLinearInInterpolator()
            addUpdateListener {
                val f = it.animatedFraction
                subProgress = if (enableCell)
                    f
                else
                    1f - f
            }
            start()
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        var buttonsHorizontalCenter = measuredWidth / 2

        /**
         * 要將動畫初始位置放在螢幕外面，開始跑動畫再進入畫面
          */
        var nextY = mViewHeight - mBaseBlockHeight - mButtonSpacing + measuredHeight
        for (i in 0..childCount) {
            var child = getChildAt(i)
            child?.let {
                if (it.tag.toString().contains("Nav")) {
                    /**
                     * 先隱藏子項目
                     */
//                    it.visibility = View.INVISIBLE
                    var childX = buttonsHorizontalCenter - it.measuredWidth / 2
                    var childY = (nextY - it.measuredHeight).toInt()
                    it.layout(childX, childY, childX + it.measuredWidth, childY + it.measuredHeight)
//                    Log.e("Ian","[layoutPosition] left:$childX, top:$childY, right:${childX+it.measuredWidth}, bottom:${childY+it.measuredHeight}")
                    val collapsedTranslation: Float = fl.y - childY.toFloat()
                    val expandedTranslation = 0f
                    //子view的動畫相關
                    if (!mSubItemList.contains(it))
                        mSubItemList.add(it)

                    nextY = (childY - mButtonSpacing)
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    class LayoutParams(source: RelativeLayout.LayoutParams?) : RelativeLayout.LayoutParams(source) {
        public val mExpandDir = ObjectAnimator()
        public val mExpandAlpha = ObjectAnimator()
        public val mCollapseDir = ObjectAnimator()
        public val mCollapseAlpha = ObjectAnimator()
        private var animationsSetToPlay = false
        fun setAnimationsTarget(view: View) {
            mCollapseAlpha.target = view
            mCollapseDir.target = view
            mExpandAlpha.target = view
            mExpandDir.target = view
            // Now that the animations have targets, set them to be played
            if (!animationsSetToPlay) {
                addLayerTypeListener(mExpandDir, view)
                addLayerTypeListener(mCollapseDir, view)
                mCollapseAnimation.play(mCollapseAlpha)
                mCollapseAnimation.play(mCollapseDir)
                mExpandAnimation.play(mExpandAlpha)
                mExpandAnimation.play(mExpandDir)
                animationsSetToPlay = true
            }
        }

        private fun addLayerTypeListener(animator: Animator, view: View) {
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.setLayerType(View.LAYER_TYPE_NONE, null)
                }

                override fun onAnimationStart(animation: Animator) {
                    view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
                }
            })
        }

        init {
            mExpandDir.interpolator = sExpandInterpolator
            mExpandAlpha.interpolator = sAlphaExpandInterpolator
            mCollapseDir.interpolator = sCollapseInterpolator
            mCollapseAlpha.interpolator = sCollapseInterpolator
            mCollapseAlpha.setProperty(View.ALPHA)
            mCollapseAlpha.setFloatValues(1f, 0f)
            mExpandAlpha.setProperty(View.ALPHA)
            mExpandAlpha.setFloatValues(0f, 1f)
//            when (mExpandDirection) {
//                FloatingActionsMenu.EXPAND_UP, FloatingActionsMenu.EXPAND_DOWN -> {
            mCollapseDir.setProperty(View.TRANSLATION_Y)
            mExpandDir.setProperty(View.TRANSLATION_Y)
//                }
//                FloatingActionsMenu.EXPAND_LEFT, FloatingActionsMenu.EXPAND_RIGHT -> {
//                    mCollapseDir.setProperty(View.TRANSLATION_X)
//                    mExpandDir.setProperty(View.TRANSLATION_X)
//                }
//            }
        }
    }
}