package com.example.page_bet.lottery_result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.repository.Repository
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.HistoricalResponse
import kotlinx.coroutines.flow.map

class LotteryResultViewModel (private var repository: Repository) : ViewModel() {

//    fun getLotteryRecord(token: String, gameId: Int): LiveData<ViewState<HistoricalResponse>> {
//        return repository.getLotteryHistoricalRecord(token, gameId, 10).map { state->
//            if (state is ViewState.Success) {
//
//            }
//            state
//        }.asLiveData()
//    }

    fun getLotteryHistoricalRecord(token: String, gameId: Int): LiveData<ViewState<HistoricalResponse>>{
        return repository.getLotteryHistoricalRecord(token, gameId).asLiveData()
    }
}