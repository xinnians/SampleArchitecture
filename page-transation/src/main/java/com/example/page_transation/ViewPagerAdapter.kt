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
        private const val CASH_PAGE = 1
        private const val BALANCE_PAGE = 2
        private const val TRANSFER_PAGE = 3
    }

    var mFakeData: TransationFragment.FakeUserCash ?= null
    var mFakeDataDetail: MutableList<TransationFragment.FakeUserCashDetail> ?= null
    var mTransAdapter: TransAdapter
    lateinit var bottomBehavior: BottomSheetBehavior<View>
    var bottomBehaviorList: MutableMap<Int, BottomSheetBehavior<View>> = mutableMapOf()
    var mContext: Context
    val tab: TabLayout
    private var slideOffsetAction: ((position: Int, slideOffset: Float)->Unit) ?= null
    private var slideStateChangeAction: ((position: Int,slideState: Int)->Unit) ?= null
    private var slideStateAction: ((sheet: BottomSheetBehavior<View>) -> Unit) ?= null

    init {
        mFakeData = fakeData
        mFakeDataDetail = fakeData.data
        Log.d("msg", "fakeData size: ${fakeData.data.size}")
        mTransAdapter = TransAdapter(mFakeDataDetail!!)
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
            CASH_PAGE -> {
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
                view.viewTreeObserver.addOnGlobalLayoutListener {
                    var height = view.height
                    var setOff = dpToPx(34f+76f, mContext)
                    bottomBehavior.peekHeight = (height - view.constrainRecords.height - setOff).toInt()

                }
                bottomBehavior = BottomSheetBehavior.from(view.bottom_sheet)
                bottomBehaviorList.put(position, bottomBehavior)
                slideStateAction?.invoke(bottomBehavior)
                bottomBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        slideOffsetAction?.invoke(position, slideOffset)
                    }
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
//                        Log.d("msg", "position: ${position} ,newState: ${newState}")
                        when(newState) {
                            BottomSheetBehavior.STATE_HIDDEN ->{Log.d("msg", "newState: STATE_HIDDEN")}
                            BottomSheetBehavior.STATE_HALF_EXPANDED -> {Log.d("msg", "newState: STATE_HALF_EXPANDED")}
                            BottomSheetBehavior.STATE_COLLAPSED -> {
                                // state = 4
                                Log.d("msg", "position: ${position}, newState: STATE_COLLAPSED")
                                slideStateChangeAction?.invoke(position, BottomSheetBehavior.STATE_COLLAPSED)

                            }
                            BottomSheetBehavior.STATE_EXPANDED -> {
                                // state = 3
                                Log.d("msg", "position: ${position}, newState: STATE_EXPANDED")
                                slideStateChangeAction?.invoke(position, BottomSheetBehavior.STATE_EXPANDED)

                            }
                        }
                    }

                })
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
                view.viewTreeObserver.addOnGlobalLayoutListener {
                    /**
                     * 計算 bottomSheet 起始位置
                     * 1.整個頁面高度 - 中間 LinearLayout 高度 - bottomSheet到LinearLayout的 marginTop(34)
                      */
                    var height = view.height
                    var setOff = dpToPx(34f+76f, mContext)
                    bottomBehavior.peekHeight = (height - view.constrainRecords.height - setOff).toInt()
                }
                bottomBehavior = BottomSheetBehavior.from(view.bottom_sheet)
                bottomBehaviorList.put(position, bottomBehavior)
                slideStateAction?.invoke(bottomBehavior)
                bottomBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        slideOffsetAction?.invoke(position, slideOffset)
                    }
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
//                            Log.d("msg", "position: ${position} ,newState: ${newState}")
                        when(newState) {
                            BottomSheetBehavior.STATE_HIDDEN ->{Log.d("msg", "newState: STATE_HIDDEN")}
                            BottomSheetBehavior.STATE_HALF_EXPANDED -> {Log.d("msg", "newState: STATE_HALF_EXPANDED")}
                            BottomSheetBehavior.STATE_COLLAPSED -> {
                                // state = 4
                                Log.d("msg", "position: ${position}, newState: STATE_COLLAPSED")
                                slideStateChangeAction?.invoke(position, BottomSheetBehavior.STATE_COLLAPSED)
                            }
                            BottomSheetBehavior.STATE_EXPANDED -> {
                                // state = 3
                                Log.d("msg", "position: ${position}, newState: STATE_EXPANDED")
                                slideStateChangeAction?.invoke(position, BottomSheetBehavior.STATE_EXPANDED)
                            }
                        }
                    }

                })
                return view
            }
        }
    }

    fun setSlideOffset(action: (position: Int, slideOffset: Float) -> Unit) {
        this.slideOffsetAction = action
    }

    fun stateChangedCallback(action: (position: Int, slideState: Int) -> Unit) {
        this.slideStateChangeAction = action
    }

    fun setSlideState(action: (sheet: BottomSheetBehavior<View>) -> Unit) {
        this.slideStateAction = action
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        Log.d("msg", "destroyItem")
        bottomBehaviorList.remove(position)
        container.removeView(any as View)
    }
}