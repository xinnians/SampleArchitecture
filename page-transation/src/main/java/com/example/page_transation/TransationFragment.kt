package com.example.page_transation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.base.BaseActivity
import com.example.base.BaseFragment
import com.example.base.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_transation.*
import me.vponomarenko.injectionmanager.x.XInjectionManager
import kotlin.random.Random

class TransationFragment : BaseFragment() {

    companion object {
        private const val ALL = 0
        private const val CASH = 1
        private const val BALANCE = 2
        private const val TRANSFER = 3
        private const val PROXY = 4
        private const val BOUNS = 5
        private const val ADJUSTMENT = 6
    }

    private var isHide = true
    private var mViewPagerAdapter: ViewPagerAdapter? = null
    private lateinit var mTransationViewModel: TransationViewModel
    private lateinit var mFakeUserCash: FakeUserCash
    private var unSelectTab: Int = 0
    private var transTemp: Int = 0
    private var nowPosition = 0
    private var finalSlideState: Int = 4
    private var tabImgHeight: Int = 0

    private val navigation: TransationNavigation by lazy {
        XInjectionManager.findComponent<TransationNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_transation, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFakeData()
        initView()
        setLinstener()
    }

    @SuppressLint("ResourceType")
    private fun initView() {
        (activity as BaseActivity?)?.showBottomNav()
        tabTransationType.setSelectedTabIndicator(Color.TRANSPARENT)
        for(index in 0..6) {
            var tab = tabTransationType.getTabAt(index)
            tab?.setCustomView(R.layout.item_transation_tab)
            var holder = ViewHolder(tab?.customView!!)
            holder.imgTabItem?.background = resources.getDrawable(R.drawable.bg_circle)
            holder.imgTabItem?.setImageDrawable(resources.getDrawable(R.drawable.ic_balance_account))
            holder.tvTabItem?.setTextColor(Color.parseColor("#7c7e83"))

            when(index) {
                ALL -> {
                    holder.imgTabItem?.background = resources.getDrawable(R.drawable.bg_selected_circle)
                    holder.imgTabItem?.setImageDrawable(resources.getDrawable(R.drawable.ic_selected_balance_account))
                    holder.tvTabItem?.setTextColor(Color.BLACK)
                    holder.tvTabItem?.text = "全部"
                    tvTransTitle.text = "全部交易"
                    holder.imgTabItem?.viewTreeObserver?.addOnGlobalLayoutListener{
                        // 計算 tabBar 圖片的高度
                        tabImgHeight = holder?.imgTabItem?.height!!
//                        Log.d("msg", "tabImgHeight: ${tabImgHeight}")
                    }
                }
                CASH -> { holder.tvTabItem?.text = "資金" }
                BALANCE -> { holder.tvTabItem?.text = "盈虧" }
                TRANSFER -> { holder.tvTabItem?.text = "轉帳" }
                PROXY -> { holder.tvTabItem?.text = "代理" }
                BOUNS -> { holder.tvTabItem?.text = "彩金" }
                ADJUSTMENT -> { holder.tvTabItem?.text = "調整" }
            }
        }
        mViewPagerAdapter = context?.let { ViewPagerAdapter(tabTransationType,it, mFakeUserCash) }
        pagerTransation.adapter = this.mViewPagerAdapter
        pagerTransation.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabTransationType))
        tabTransationType.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pagerTransation))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setLinstener() {
        // bottomSheet 被滑動時
        mViewPagerAdapter?.setSlideOffset { position: Int, progress: Float ->
            // tabBar 需要位移的高度為圖片高度 + 文字與圖片的 marginTop 距離
            var translateHeight = context?.let { dpToPx(11f, it) + tabImgHeight.toFloat() }
            var trans = ((progress * translateHeight!!) % translateHeight+1).toInt()
//            Log.d("msg", "slideOffset: ${position}, progress: ${trans}")
            /**
             * 判斷是否移動 tabBar
             * 1.偏移量是否重複
             * 2.是否為當前 tab，不是當前 tab 則不移動
             * 3.偏移量是否為 1
             */
            if(trans != transTemp && position == nowPosition && trans != 1) {
//                Log.d("msg", "slideOffset: ${position}, progress: ${trans}")
                startTranslateY(tabTransationType, trans)
                transTemp = trans
            }
        }
        // bottomSheet 狀態改變時
        mViewPagerAdapter?.stateChangedCallback { position, slideState ->
            /**
             * 當 bottomSheet 狀態改變時要把所有頁面的狀態調整為一致
             * 1.判斷所有頁面的 bottomSheet 狀態是否與當前狀態一致
             * 2.判斷是否為當前頁面的 bottomSheet，只需要改變其他頁面狀態，當前頁面的已經更動
             */
            mViewPagerAdapter?.bottomBehaviorList?.forEach { t, u ->
                if(u.state != slideState && t != nowPosition) {
                    u.state = slideState
                }
            }
            finalSlideState = slideState
        }
        // 初始化 bottomSheet 狀態
        mViewPagerAdapter?.setSlideState {
            /**
             * 初始化 bottomSheet 時要根據當前頁面的狀態設定
             */
            when(finalSlideState) {
                BottomSheetBehavior.STATE_EXPANDED -> {it.state = BottomSheetBehavior.STATE_EXPANDED}
                BottomSheetBehavior.STATE_COLLAPSED -> {it.state = BottomSheetBehavior.STATE_COLLAPSED}
            }
        }
        tabTransationType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // TODO
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("msg", "onTabUnselected: ${tab?.position}")
                var holder = ViewHolder(tab?.customView!!)
                holder.imgTabItem?.background = resources.getDrawable(R.drawable.bg_circle)
                holder.imgTabItem?.setImageDrawable(resources.getDrawable(R.drawable.ic_balance_account))
                holder.tvTabItem?.setTextColor(Color.parseColor("#7c7e83"))
                unSelectTab = tab.position
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("msg", "onTabSelected ${tab?.position}")
                var holder = ViewHolder(tab?.customView!!)
                holder.imgTabItem?.background = resources.getDrawable(R.drawable.bg_selected_circle)
                holder.imgTabItem?.setImageDrawable(resources.getDrawable(R.drawable.ic_selected_balance_account))
                holder.tvTabItem?.setTextColor(Color.BLACK)
                tvTransTitle.text = tab.text
                nowPosition = tab.position
                // 更換頁面要重新顯示 tabBar
//                Log.d("msg", "tab stage: ${selectTabState[tab.position]}")
            }
        })
    }

    class ViewHolder(view:View) : TabLayout.Tab() {
        var imgTabItem:ImageView?=null
        var tvTabItem:TextView?=null
        init {
            imgTabItem = view.findViewById(R.id.imgTabItem)
            tvTabItem = view.findViewById(R.id.tvTabItem)
        }
    }

    fun startTranslateY(view: View, translateY: Int){
//        val transY = context?.let { dpToPx(translateY.toFloat(), it) }
//        Log.d("msg", "translateY: ${translateY.toFloat()}")
        val currentY: Float= view.translationY
        view.translationY = -translateY.toFloat()
    }

    fun setFakeData() {
        var datalist = mutableListOf<FakeUserCashDetail>()
        for(index in 0..20) {
            var gameType = ""
            var transDate = "2020/06/18"
            var transAmount = Random.nextInt(0,100000)
            var randomType = Random.nextInt(0,2)
            when(randomType) {
                0 -> { gameType = "盈虧-彩票-歡樂生肖" }
                1 -> { gameType = "資金-彩票-歡樂生肖" }
                2 -> { gameType = "轉帳-彩票-歡樂生肖" }
            }
            var fakeDetail = FakeUserCashDetail(gameType, transDate, transAmount)
            datalist.add(fakeDetail)
        }
        mFakeUserCash = FakeUserCash(123456, 2345, 23456, datalist)
    }

    data class FakeUserCashDetail(var gameType:String, var transDate:String, var transAmount:Int)
    data class FakeUserCash(var totalCash:Int, var withdraw:Int, var lockCash:Int, var data:MutableList<FakeUserCashDetail>)

}