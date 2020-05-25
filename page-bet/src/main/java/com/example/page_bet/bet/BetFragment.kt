package com.example.page_bet.bet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.observeNotNull
import com.example.base.timer
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.example.repository.model.MultipleIssueResultItem
import com.example.repository.model.MultipleIssueResultItem.Companion.TIME_LOTTERY
import com.example.repository.model.ViewState
import kotlinx.android.synthetic.main.fragment_bet.*
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.x.XInjectionManager
import kotlin.concurrent.timer

class BetFragment : BaseFragment() {

    companion object {
        const val TAG_GAME_ID = "tag_game_id"
        const val TAG_GAME_NAME = "tag_game_name"
        const val TAG_GAME_TYPE = "tag_game_type"
    }

    private lateinit var mViewModel: BetViewModel

    private var mGameID: Int = -1
    private var mGameTypeID: Int = -1

    private val navigation: BetNavigation by lazy {
        XInjectionManager.findComponent<BetNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_bet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        Log.e("Ian", "[BetFragment] init")
        mViewModel = AppInjector.obtainViewModel(this)
        arguments?.let {
            ivGameName.text = it.getString(TAG_GAME_NAME, "empty")
            mGameID = it.getInt(TAG_GAME_ID, -1)
            mGameTypeID = it.getInt(TAG_GAME_TYPE, -1)
        }
        refreshCurrentIssueInfo(getSharedViewModel().lotteryToken.value ?: "empty", mGameID)
        refreshLastIssueResult(getSharedViewModel().lotteryToken.value?: "empty", mGameID, mGameTypeID)
    }

    private fun refreshCurrentIssueInfo(token: String, gameId: Int) {
        Log.e("Ian", "[refreshCurrentIssueInfo] token:$token, gameId:$gameId")
        mViewModel.getCurrentIssueInfo(token, gameId)
            .observeNotNull(this) { state ->
                when (state) {
                    is ViewState.Success -> {
                        tvCurrentIssueNumber.text = "${state.data.data.issueNum.substring(5)}期"
                        var leftTime = ((state.data.data.buyEndTime - System.currentTimeMillis()) / 1000).toInt()
                        launch {
                            timer(1000, false) {
                                var time = leftTime--
                                tvCurrentIssueLeftTime.text = getDisplayTime(time)
                                if (time <= 0) {
                                    this.cancel()
                                    refreshCurrentIssueInfo(
                                        getSharedViewModel().lotteryToken.value ?: "empty", mGameID
                                    )
                                    refreshLastIssueResult(getSharedViewModel().lotteryToken.value ?: "empty", mGameID,mGameTypeID)
                                }
                            }
                        }
                    }
                    is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
                    is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
                }
            }
    }

    private fun refreshLastIssueResult(token: String, gameId: Int, gameTypeId: Int) {
        Log.e("Ian","[refreshLastIssueResult] token:$token, gameId:$gameId gameTypeId:$gameTypeId")
        mViewModel.getLastIssueResult(token, gameId)
            .observeNotNull(this){ state ->
                when(state){
                    is ViewState.Success -> {
                        //TODO 根據gametype切換adapter中的item顯示
                        Log.e("Ian", "[refreshLastIssueResult] Success data:${state.data}")

                        tvLastIssueNumber.text = "${state.data.data[0].issueNum.substring(5)}期"

                        var layoutManager = LinearLayoutManager(context)
                        layoutManager.orientation = LinearLayoutManager.VERTICAL
                        rvLastIssueResult.layoutManager = layoutManager

                        var adapter: IssueResultAdapter = IssueResultAdapter(mutableListOf(
                            MultipleIssueResultItem(gameTypeId, state.data.data[0])
                        ))
                        rvLastIssueResult.adapter = adapter
                    }
                    is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
                    is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
                }
            }
    }

    private fun getDisplayTime(second: Int): String {
        var h = 0
        var d = 0
        var s = 0
        val temp = second % 3600
        if (second > 3600) {
            h = second / 3600
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60
                    if (temp % 60 != 0) {
                        s = temp % 60
                    }
                } else {
                    s = temp
                }
            }
        } else {
            d = second / 60
            if (second % 60 !== 0) {
                s = second % 60
            }
        }
        return "$h : $d : $s"
    }
}