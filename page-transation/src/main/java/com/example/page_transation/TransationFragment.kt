package com.example.page_transation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.base.BaseActivity
import com.example.base.BaseFragment
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
                }
                CASH -> { holder.tvTabItem?.text = "資金" }
                BALANCE -> { holder.tvTabItem?.text = "盈虧" }
                TRANSFER -> { holder.tvTabItem?.text = "轉帳" }
                PROXY -> { holder.tvTabItem?.text = "代理" }
                BOUNS -> { holder.tvTabItem?.text = "彩金" }
                ADJUSTMENT -> { holder.tvTabItem?.text = "調整" }
            }
        }
        mViewPagerAdapter = ViewPagerAdapter(mFakeUserCash)
        pagerTransation.adapter = this.mViewPagerAdapter
        pagerTransation.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabTransationType))
        tabTransationType.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pagerTransation))
    }

    fun setLinstener() {
        tabTransationType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // TODO
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("msg", "onTabUnselected")
                var holder = ViewHolder(tab?.customView!!)
                holder.imgTabItem?.background = resources.getDrawable(R.drawable.bg_circle)
                holder.imgTabItem?.setImageDrawable(resources.getDrawable(R.drawable.ic_balance_account))
                holder.tvTabItem?.setTextColor(Color.parseColor("#7c7e83"))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("msg", "onTabSelected")
                var holder = ViewHolder(tab?.customView!!)
                holder.imgTabItem?.background = resources.getDrawable(R.drawable.bg_selected_circle)
                holder.imgTabItem?.setImageDrawable(resources.getDrawable(R.drawable.ic_selected_balance_account))
                holder.tvTabItem?.setTextColor(Color.BLACK)
                tvTransTitle.text = tab.text
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