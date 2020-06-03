package com.example.page_bet.lottery_center

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.BaseFragment
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_lottery_center.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

/**
 * 目前的預想是進來開獎中心後，即統一拉所有遊戲最新的開獎訊息，再點擊tabLayout選擇遊戲類別都僅顯示剛剛查詢資料，不再額外作查詢的動作
 *
 * TODO question1.需要每次跟server確認要顯示哪幾個類別嗎?還是固定就好? 目前以固定的方式去實作畫面
 */
class LotteryCenterFragment : BaseFragment() {

    private lateinit var mLotteryCenterViewModel: LotteryCenterViewModel

    private val navigation: BetNavigation by lazy {
        XInjectionManager.findComponent<BetNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_lottery_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    private fun initView() {
        tlLotteryType.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.e("Ian","[onTabReselected] tab:${tab?.position}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.e("Ian","[onTabUnselected] tab:${tab?.position}")
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.e("Ian","[onTabSelected] tab:${tab?.position}")
            }

        })
    }

    private fun initData() {
    }
}