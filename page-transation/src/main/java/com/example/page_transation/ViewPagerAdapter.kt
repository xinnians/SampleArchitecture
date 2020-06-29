package com.example.page_transation

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.base.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.include_bottom_sheet.view.*
import kotlinx.android.synthetic.main.page_all.view.*
import kotlinx.android.synthetic.main.page_balance.view.*


class ViewPagerAdapter(context: Context, fakeData: TransationFragment.FakeUserCash) : PagerAdapter() {

    companion object {
        private const val CASH_PAGE = 1
        private const val BALANCE_PAGE = 2
        private const val TRANSFER_PAGE = 3
    }

    var mFakeData: TransationFragment.FakeUserCash ?= null
    var mFakeDataDetail: MutableList<TransationFragment.FakeUserCashDetail> ?= null
    var mFakeUserGame: MutableList<TransationFragment.FakeUserGame> ?= null

    var mTransAdapter: TransAdapter
    var mGameAdapter: GamePagerAdapter
    lateinit var bottomBehavior: BottomSheetBehavior<View>
    var bottomBehaviorList: MutableMap<Int, BottomSheetBehavior<View>> = mutableMapOf()
    var imgGuideList: MutableList<ImageView> = mutableListOf()
    var mContext: Context
    private var slideOffsetAction: ((position: Int, slideOffset: Float)->Unit) ?= null
    private var slideStateChangeAction: ((position: Int,slideState: Int)->Unit) ?= null
    private var slideStateAction: ((sheet: BottomSheetBehavior<View>) -> Unit) ?= null
    // bottomSheet 到頂端的距離(不含中間 cashView 的高度)
    private var sheetToTitleOffsetHeight: Float = 34f+92f
    private var sheetToTitleOffsetHeight2: Float = 34f+92f+44f+50f

    init {
        mFakeData = fakeData
        mFakeDataDetail = fakeData.data
        mFakeUserGame = fakeData.gameData
        Log.d("msg", "fakeData size: ${fakeData.data.size}")
        mTransAdapter = TransAdapter(mFakeDataDetail!!)
        this.mContext = context
        mGameAdapter = GamePagerAdapter(mContext, mFakeUserGame!!)
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
                view.pagerGameName.adapter = mGameAdapter
                view.pagerGameName.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                    override fun onPageScrollStateChanged(state: Int) {
                        // TODO
                    }

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                        // TODO
                    }

                    override fun onPageSelected(position: Int) {
                        // TODO
                        Log.d("msg", "onPageSelected: ${position}")
                        imgGuideList.forEachIndexed { index, imageView ->
                            Log.d("msg", "index: ${index}")
                            if(index == position) {
                                imageView.setImageResource(R.drawable.bg_selected_page_guilde)
                            } else {
                                imageView.setImageResource(R.drawable.bg_page_guilde)
                            }
                        }
                    }
                })
                initGameViewPagerGuide(view.llGuidePage)
                view.tvFliterDate.text = getPageTitle(position)
//                view.tvTotalAssets.text = mFakeData?.totalCash.toString()
//                view.tvWithdraw.text = mFakeData?.withdraw.toString()
//                view.tvLockCash.text = mFakeData?.lockCash.toString()
                var layoutManager = LinearLayoutManager(view.context)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                view.recyclerTrans.layoutManager = layoutManager
                view.recyclerTrans.adapter = mTransAdapter
                bottomBehavior = BottomSheetBehavior.from(view.bottom_sheet)

                view.viewTreeObserver.addOnGlobalLayoutListener {
                    var height = view.height
                    var offset = dpToPx(sheetToTitleOffsetHeight, mContext)
                    bottomBehavior.peekHeight = (height - view.constrainRecords.height - offset).toInt()
//                    Log.d("msg", "page: balance, peekHeight: ${bottomBehavior.peekHeight}")
                }
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
                bottomBehavior = BottomSheetBehavior.from(view.bottom_sheet)
                view.viewTreeObserver.addOnGlobalLayoutListener {
//                    var height = view.height
//                    var offset = dpToPx(sheetToTitleOffsetHeight, mContext)
//                    bottomBehavior.peekHeight = (height - view.constrainRecords.height - offset).toInt()
//                    Log.d("msg", "view.height: ${height}, record.height: ${view.constrainRecords.height} ,offset: ${offset}")
//                    Log.d("msg", "page: cash, peekHeight: ${bottomBehavior.peekHeight}")
                }
                Log.d("msg", "page: cash, first peekHeight: ${bottomBehavior.peekHeight}")
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
                bottomBehavior = BottomSheetBehavior.from(view.bottom_sheet)
                view.viewTreeObserver.addOnGlobalLayoutListener {
                    /**
                     * 計算 bottomSheet 起始位置
                     * 1.整個頁面高度 - 中間 LinearLayout 高度 - bottomSheet到LinearLayout的 marginTop(34) - offset
                     *
                      */
//                    var height = view.height
//                    var offset = dpToPx(sheetToTitleOffsetHeight, mContext)
//                    bottomBehavior.peekHeight = (height - view.constrainRecords.height - offset).toInt()
//                    Log.d("msg", "page: else, peekHeight: ${bottomBehavior.peekHeight}")
                }
                view.post(Runnable {
                    var height = view.height
                    Log.d("msg", "view width: ${height}")
                    var offset = dpToPx(sheetToTitleOffsetHeight, mContext)
                    bottomBehavior.peekHeight = (height - view.constrainRecords.height - offset).toInt()
                })
//                val dm2: DisplayMetrics = mContext.getResources().getDisplayMetrics()
//                var recordViewHeight = dpToPx(100f, mContext).toInt()
//                var offset = dpToPx(sheetToTitleOffsetHeight2, mContext).toInt()
//                bottomBehavior.peekHeight = dm2.heightPixels - recordViewHeight - offset
//                Log.d("msg", "display: ${dm2.heightPixels}")
//                Log.d("msg", "recordViewHeight: ${recordViewHeight}, offset: ${offset}")
//                Log.d("msg", "peekHeight: ${bottomBehavior.peekHeight}")
//                bottomBehavior.peekHeight = 1346
                Log.d("msg", "page else first peekHeight: ${bottomBehavior.peekHeight}")

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

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        Log.d("msg", "destroyItem")
        bottomBehaviorList.remove(position)
        if(position == BALANCE_PAGE) {
            imgGuideList.clear()
        }
        container.removeView(any as View)
    }

    // 產生 pager guilde
    fun initGameViewPagerGuide(view: LinearLayout) {
        var pagerCount = mFakeUserGame?.size?.div(3)
        Log.d("msg", "pagerCount: ${pagerCount}")
        var guideWidth = dpToPx(6.0f, mContext).toInt()
        var layoutParam = LinearLayout.LayoutParams(guideWidth,guideWidth)
        layoutParam.leftMargin = 5
        for(index in 1..pagerCount!!) {
            var imageView = ImageView(mContext)
            if(index == 1) {
                imageView.setImageResource(R.drawable.bg_selected_page_guilde)
            } else {
                imageView.setImageResource(R.drawable.bg_page_guilde)
            }
            imageView.layoutParams = layoutParam
            imgGuideList.add(imageView)
            view.addView(imageView)
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

}