package com.example.page_bet.lottery_result

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.observeNotNull
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.example.page_bet.bet.BetFragment
import com.example.repository.constant.GameTypeId
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.MultipleHistoryRecord
import kotlinx.android.synthetic.main.fragment_lottery_result.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class LotteryResultFragment: BaseFragment() {

    private lateinit var mLotteryResultViewModel: LotteryResultViewModel
    private var gameId:Int = -1
    private var resultAdapter: LotteryResultAdapter ?= null
    private var list: MutableList<MultipleHistoryRecord> = mutableListOf()

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
        initView()
        setListener()
        initData()
    }

    fun initView() {
        var layoutManager = LinearLayoutManager(context)
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
                    state.data.data.forEach {
                        list.add(MultipleHistoryRecord(gameId.toString().substring(0,1).toInt(),it))
                    }
                    resultAdapter = context?.let { LotteryResultAdapter(list, it) }
                    resultRecycleLayout.adapter = resultAdapter
                }
                is ViewState.Loading -> Log.d("msg", "ViewState.Loading")
                is ViewState.Error -> Log.d("msg", "ViewState.Error : ${state.message}")
            }
        }
    }

    fun setListener() {
        etNumSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) { }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var newList: MutableList<MultipleHistoryRecord> = mutableListOf()
                for(item in list) {
                    if(s?.let { item.data.issueNum.contains(it) }!!) {
                        newList.add(item)
                    }
                }
                resultAdapter?.setNewData(newList)
                resultAdapter?.notifyDataSetChanged()
            }
        })
    }
}