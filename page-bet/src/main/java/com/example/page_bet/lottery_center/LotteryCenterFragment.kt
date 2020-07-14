package com.example.page_bet.lottery_center

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.observeNotNull
import com.example.base.onClick
import com.example.base.widget.CustomTitleBar
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.example.page_bet.bet.BetFragment
import com.example.repository.constant.GameTypeId
import com.example.repository.model.base.ViewState
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_lottery_center.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

/**
 * 目前的預想是進來開獎中心後，即統一拉所有遊戲最新的開獎訊息，點擊tabLayout選擇遊戲類別都僅顯示剛剛查詢資料，不再額外作查詢的動作
 * 需先整理遊戲選單中所有遊戲類型下的遊戲ID進行查詢，在選擇遊戲類別時僅顯示該類型下的遊戲ID
 *
 * TODO question1.需要每次跟server確認要顯示哪幾個類別嗎?還是固定就好? 目前以固定的方式去實作畫面
 */
class LotteryCenterFragment : BaseFragment() {

    private lateinit var mLotteryCenterViewModel: LotteryCenterViewModel
    private var mAdapter: ViewPagerAdapter? = null

    private val navigation: BetNavigation by lazy {
        XInjectionManager.findComponent<BetNavigation>()
    }

    private var mCurrentGameType: GameTypeId = GameTypeId.RACING

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_lottery_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {

        tlLotteryType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.e("Ian", "[onTabReselected] tab:${tab?.position}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.e("Ian", "[onTabUnselected] tab:${tab?.position}")
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.e("Ian", "[onTabSelected] tab:${tab?.position}")
//                tab?.position?.let { tabPosition ->
//                    mCurrentGameType = GameTypeId.values().first { it.typeId == (tabPosition + 1) }
//                    mAdapter?.setNewData(mLotteryCenterViewModel.getIssueInfoList(mCurrentGameType))
//                }

                Log.e("Ian", "[onTabSelected] CurrentGameType: $mCurrentGameType")

            }
        })

        mAdapter= ViewPagerAdapter()
        mAdapter!!.setResultAction {
            var bundle = Bundle()
            it.data?.gameId?.let { it1 -> bundle.putInt(BetFragment.TAG_GAME_ID, it1) }
            navigation.toLotteryResultPage(bundle)
        }
//        var layoutManager: LinearLayoutManager = LinearLayoutManager(context)
//        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        rvGameIssueInfo.layoutManager = layoutManager
        vpGameIssueInfo.adapter = mAdapter
        TabLayoutMediator(tlLotteryType,vpGameIssueInfo) { tab: TabLayout.Tab, i: Int ->
            tab.text = GameTypeId.values()[i].chineseName
        }.attach()

        toolbar.backListener(View.OnClickListener { navigation.backPrePage() })

    }

    private fun initData() {
        mLotteryCenterViewModel = AppInjector.obtainViewModel(this)

        getSharedViewModel().gameMenuList.value?.let {
            mLotteryCenterViewModel.setGameMenuList(it)
        }

        mLotteryCenterViewModel.getAllGameLastIssueInfo(getSharedViewModel().lotteryToken.value!!).observeNotNull(this){state ->
            when (state) {
                is ViewState.Success -> {
                    Log.e("Ian","${state.data.data}")
//                    mAdapter?.setNewData(mLotteryCenterViewModel.getIssueInfoList(mCurrentGameType))
                    mAdapter?.mData = mLotteryCenterViewModel.getAllIssueInfoList()
                    mAdapter?.notifyDataSetChanged()
                }
                is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
                is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
            }
        }
    }
}