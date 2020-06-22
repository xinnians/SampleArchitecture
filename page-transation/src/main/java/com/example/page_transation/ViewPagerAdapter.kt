package com.example.page_transation

import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.example.base.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.include_bottom_sheet.view.*
import kotlinx.android.synthetic.main.page_all.view.*


class ViewPagerAdapter(tab:TabLayout, context: Context, fakeData: TransationFragment.FakeUserCash) : PagerAdapter() {

    companion object {
        private const val BALANCE_PAGE = 2
        private const val TRANSFER_PAGE = 3
    }

    var mFakeData: TransationFragment.FakeUserCash ?= null
    var mFakeDataDetail: MutableList<TransationFragment.FakeUserCashDetail> ?= null
    var mTransAdapter: TransAdapter
    lateinit var bottomBehavior: BottomSheetBehavior<View>
//    var windowHieght: Int
    var mContext: Context
    val tab: TabLayout
    private var alphaAnimation: AlphaAnimation? = null
    private var slideOffsetAction: ((slideOffset: Float)->Unit) ? =null

    init {
        mFakeData = fakeData
        mFakeDataDetail = fakeData.data
        Log.d("msg", "fakeData size: ${fakeData.data.size}")
        mTransAdapter = TransAdapter(mFakeDataDetail!!)
//        this.windowHieght = windowHieght
        this.mContext = context
        this.tab = tab
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return o == view
    }

    override fun getCount(): Int {
        return 7
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Page: Item${position}"
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        Log.d("msg", "instantiateItem position: ${position}")
        when(position) {
            BALANCE_PAGE -> {
                val view = LayoutInflater.from(container.context).inflate(R.layout.page_balance, container, false)
                container.addView(view)
                return view
            }
            TRANSFER_PAGE -> {
                val view = LayoutInflater.from(container.context).inflate(R.layout.page_transfer, container, false)
                container.addView(view)
                return view
            }
            else -> {
                val view = LayoutInflater.from(container.context).inflate(R.layout.page_all, container, false)
                container.addView(view)
                view.tvFliterDate.text = getPageTitle(position)
                view.tvTotalAssets.text = mFakeData?.totalCash.toString()
                view.tvWithdraw.text = mFakeData?.withdraw.toString()
                view.tvLockCash.text = mFakeData?.lockCash.toString()
                var layoutManager = LinearLayoutManager(view.context)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                view.recyclerTrans.layoutManager = layoutManager
                view.recyclerTrans.adapter = mTransAdapter

                tab.alpha = 1.0f
                alphaAnimation = AlphaAnimation(1.0f, 0.0f)
                alphaAnimation?.duration = 3000
                alphaAnimation?.fillAfter = true

                view.viewTreeObserver.addOnGlobalLayoutListener {
                    var height = view.height
//                    Log.d("msg", "height: ${height}")
                    bottomBehavior = BottomSheetBehavior.from(view.bottom_sheet)
                    var setOff = dpToPx(34f+76f, mContext)
                    bottomBehavior.peekHeight = (height - view.constrainRecords.height - setOff).toInt()
                    bottomBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                        override fun onSlide(bottomSheet: View, slideOffset: Float) {
                            Log.d("msg", "slideOffset: ${slideOffset}")
//                            if(slideOffset == 1.0f) {
////                                tab.visibility = View.INVISIBLE
//                            }
//                            if(slideOffset == 0.0f) {
////                                tab.visibility = View.VISIBLE
//                            }
//                            tab.alpha = 1.0f - 0.2f - slideOffset
                            slideOffsetAction?.invoke(slideOffset)
                        }

                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            Log.d("msg", "newState: ${newState}")
                            when(newState) {
                                BottomSheetBehavior.STATE_HIDDEN ->{Log.d("msg", "newState: STATE_HIDDEN")}
                                BottomSheetBehavior.STATE_HALF_EXPANDED -> {Log.d("msg", "newState: STATE_HALF_EXPANDED")}
                                BottomSheetBehavior.STATE_COLLAPSED -> {Log.d("msg", "newState: STATE_COLLAPSED")}
                                BottomSheetBehavior.STATE_EXPANDED -> {Log.d("msg", "newState: STATE_EXPANDED")}
                            }
                        }

                    })
//                    bottomBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
//                        override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                            Log.d("msg", "onSlide")
//                        }
//
//                        override fun onStateChanged(bottomSheet: View, newState: Int) {
//                            Log.d("msg", "onStateChanged")
//                        }
//
//                    })
                }
//                bottomBehavior = BottomSheetBehavior.from(view.bottom_sheet)
//                bottomBehavior.peekHeight = windowHieght - 384



//                view.btnShowSheet.setOnClickListener {
//                    Log.d("msg", "show")
//                    showBottomSheet()
//                }
//                view.btnHideSheet.setOnClickListener {
//                    Log.d("msg", "hide")
//                    hideBottomSheet()
//                }
                return view
            }
        }
    }

    fun setSlideOffset(action: (slideOffset: Float) -> Unit) {
        this.slideOffsetAction = action
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        Log.d("msg", "destroyItem")
        container.removeView(any as View)
    }

    fun  hideBottomSheet(){
        bottomBehavior.isHideable = true
        bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN

    }

    fun showBottomSheet() {
        bottomBehavior.isHideable = false
        setBottomViewVisible(bottomBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
    }

    private fun setBottomViewVisible(showFlag: Boolean) {
        if (showFlag)
            bottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        else
            bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}