package com.example.page_bet.lottery_center

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.observeNotNull
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.example.page_bet.bet.IssueResultAdapter
import com.example.repository.constant.GameTypeId
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.MultipleIssueResultItem
import com.google.android.material.tabs.TabLayout
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
        initData()
        initView()
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
                tab?.position?.let { tabPosition ->
                    mCurrentGameType = GameTypeId.values().first { it.typeId == (tabPosition + 1) }
                }

                Log.e("Ian", "[onTabSelected] CurrentGameType: $mCurrentGameType")

            }

        })
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
                    var adapter: IssueResultAdapter = IssueResultAdapter(mutableListOf())
                    var layoutManager: LinearLayoutManager = LinearLayoutManager(context)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL

                    rvGameIssueInfo.layoutManager = layoutManager
                    rvGameIssueInfo.adapter = adapter
                    var data: MutableList<MultipleIssueResultItem> = mutableListOf()
                    state.data.data?.forEach {
                        data.add(MultipleIssueResultItem(GameTypeId.LUCKY.typeId,it))
                    }
                    adapter.setNewData(data)
                }
                is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
                is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
            }
        }
    }
}