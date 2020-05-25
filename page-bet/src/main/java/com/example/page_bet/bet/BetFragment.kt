package com.example.page_bet.bet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.observeNotNull
import com.example.base.timer
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.example.repository.model.ViewState
import kotlinx.android.synthetic.main.fragment_bet.*
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.x.XInjectionManager
import kotlin.concurrent.timer

class BetFragment : BaseFragment() {

    companion object {
        const val TAG_GAME_ID = "tag_game_id"
        const val TAG_GAME_NAME = "tag_game_name"
    }

    private lateinit var mViewModel: BetViewModel

    private var mGameID: Int = 0

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
        Log.e("Ian","[BetFragment] init")
        mViewModel = AppInjector.obtainViewModel(this)
        arguments?.getString(TAG_GAME_NAME, "empty").let {
            ivGameName.text = it
        }
        arguments?.getInt(TAG_GAME_ID, 0)?.let {
            mGameID = it
        }
        refreshCurrentIssueInfo(getSharedViewModel().lotteryToken.value ?: "empty", mGameID)
    }

    private fun refreshCurrentIssueInfo(token: String, gameId: Int) {
        Log.e("Ian","[refreshCurrentIssueInfo] token:$token, gameId:$gameId")
        mViewModel.getCurrentIssueInfo(token, gameId)
            .observeNotNull(this) { state ->
                when (state) {
                    is ViewState.Success -> {
                        tvCurrentIssueNumber.text = state.data.data.issueNum
                        var leftTime = ((state.data.data.buyEndTime - System.currentTimeMillis()) - 1000).toInt()
                        launch {
                            timer(1000, false) {
                                var time = leftTime--
                                tvCurrentIssueLeftTime.text = getDisplayTime(time)
                                if (time <= 0) {
                                    this.cancel()
                                    refreshCurrentIssueInfo(
                                        getSharedViewModel().lotteryToken.value ?: "empty", mGameID
                                    )
                                }
                            }
                        }
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