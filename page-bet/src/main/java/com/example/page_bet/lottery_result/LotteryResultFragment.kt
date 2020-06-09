package com.example.page_bet.lottery_result

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
import com.example.page_bet.bet.BetFragment
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.BetData
import com.example.repository.model.bet.BetSelectNumber
import com.example.repository.model.bet.MultipleHistoryRecord
import kotlinx.android.synthetic.main.fragment_lottery_result.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class LotteryResultFragment: BaseFragment() {

    private lateinit var mLotteryResultViewModel: LotteryResultViewModel
    private var gameId:Int = -1
    private var resultAdapter: LotteryResultAdapter ?= null

    private val navigation: BetNavigation by lazy {
        XInjectionManager.findComponent<BetNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_lottery_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameId = arguments?.getInt(BetFragment.TAG_GAME_ID)!!
        Log.d("msg", "gameId: ${gameId}")
        initView()
        initData()
    }

    fun initView() {
        var layoutManager: LinearLayoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        resultRecycleLayout.layoutManager = layoutManager
    }

    fun initData() {
        mLotteryResultViewModel = AppInjector.obtainViewModel(this)
        var token = getSharedViewModel().lotteryToken.value!!
        mLotteryResultViewModel.getLotteryHistoricalRecord(token, gameId).observeNotNull(this){ state ->
            when (state) {
                is ViewState.Success -> {
                    Log.d("msg","[getLotteryHistoricalRecord] success: ")
                    var list: MutableList<MultipleHistoryRecord> = mutableListOf()
                    state.data.data.forEach {
                        list.add(MultipleHistoryRecord(gameId,it))
                    }
                    resultAdapter = LotteryResultAdapter(list)
                    resultRecycleLayout.adapter = resultAdapter
                }
                is ViewState.Loading -> Log.d("msg", "ViewState.Loading")
                is ViewState.Error -> Log.d("msg", "ViewState.Error : ${state.message}")
            }
        }

    }
}