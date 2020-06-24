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
import com.example.base.*
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.example.page_bet.bet.BetFragment
import com.example.repository.constant.GameTypeId
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.MultipleHistoryRecord
import kotlinx.android.synthetic.main.fragment_bet.*
import kotlinx.android.synthetic.main.fragment_lottery_result.*
import me.vponomarenko.injectionmanager.x.XInjectionManager
import java.util.ArrayList

class LotteryResultFragment: BaseFragment() {

    private lateinit var mLotteryResultViewModel: LotteryResultViewModel
    private var gameId:Int = -1
    private var gameName:String = ""
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
        initData()
        initView()
        initBinding()
        setListener()
    }

    private fun initBinding() {
        mLotteryResultViewModel.lotteryHistoryRecordData.observeNotNull(this){
            it.data.forEach{
                list.add(MultipleHistoryRecord(gameId.toString().substring(0,1).toInt(),it))
            }
            resultAdapter?.setNewData(list)
            resultRecycleLayout.adapter = resultAdapter
        }
    }

    fun initView() {
        var layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        resultRecycleLayout.layoutManager = layoutManager
        when(gameId.toString().substring(0,1).toInt()) {
            GameTypeId.RACING.typeId -> {
                gameName = GameTypeId.RACING.chineseName
                tvResultTitle.text = GameTypeId.RACING.chineseName
            }
            GameTypeId.TIME_LOTTERY.typeId -> {
                tvResultTitle.text = GameTypeId.TIME_LOTTERY.chineseName
                gameName = GameTypeId.TIME_LOTTERY.chineseName
            }
            GameTypeId.CHOOSE.typeId -> {
                tvResultTitle.text = GameTypeId.CHOOSE.chineseName
                gameName = GameTypeId.CHOOSE.chineseName
            }
            GameTypeId.HURRY_THREE.typeId -> {
                tvResultTitle.text = GameTypeId.HURRY_THREE.chineseName
                gameName = GameTypeId.HURRY_THREE.chineseName
            }
            GameTypeId.LUCKY.typeId -> {
                tvResultTitle.text = GameTypeId.LUCKY.chineseName
                gameName = GameTypeId.LUCKY.chineseName
            }
            GameTypeId.MARX_SIX.typeId -> {
                tvResultTitle.text = GameTypeId.MARX_SIX.chineseName
                gameName = GameTypeId.MARX_SIX.chineseName
            }
        }
        resultAdapter = context?.let { LotteryResultAdapter(mutableListOf(), it) }
    }

    fun initData() {
        mLotteryResultViewModel = AppInjector.obtainViewModel(this)
        var token = getSharedViewModel().lotteryToken.value!!
        gameId = arguments?.getInt(BetFragment.TAG_GAME_ID)!!
        mLotteryResultViewModel.getHistoricalRecord(token, gameId)
    }

    fun setListener() {
        ivLeftArrow.onClick { navigation?.backPrePage() }
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
        btnDateSearch.setOnClickListener {
            //TODO 點選日期搜尋
            var datePick = context?.let { it1 ->
                DatePickerDialog(it1, object:DatePickerDialog.GetDateListener{
                    override fun getSearch(list: ArrayList<Long>) {
                        //TODO 搜尋
                        list.forEach{
                            Log.d("msg", "it: ${it}")
                        }
                    }
                })
            }
            datePick?.show()
        }
        btnBetting.setOnClickListener {
            //TODO to BetFragment
            var bundle = Bundle()
            bundle.putInt(BetFragment.TAG_GAME_ID, gameId)
            bundle.putString(BetFragment.TAG_GAME_NAME, gameName)
            bundle.putInt(BetFragment.TAG_GAME_TYPE, gameId.toString().substring(0,1).toInt())
            navigation.fromResultToBetPage(bundle)
        }
    }
}