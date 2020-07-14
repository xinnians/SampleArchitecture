package com.example.page_bet.lottery_result

import androidx.lifecycle.*
import com.example.repository.Repository
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.HistoricalResponse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LotteryResultViewModel (private var repository: Repository) : ViewModel() {

    var liveLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var liveError: MutableLiveData<String> = MutableLiveData()
    // 開票歷史紀錄
    var lotteryHistoryRecordData: MutableLiveData<HistoricalResponse> = MutableLiveData()

    fun getHistoricalRecord(token: String, gameId: Int) {
        viewModelScope.launch {
            repository.getLotteryHistoricalRecord(token, gameId).collect { state ->
                when(state) {
                    is ViewState.Success -> {
                        liveLoading.value = false
                        state.data.let {
                            lotteryHistoryRecordData.value = it
                        }
                    }
                    is ViewState.Loading -> {
                        liveLoading.value = true
                    }
                    is ViewState.Error -> {
                        liveLoading.value = false
                        liveError.value = state.message
                    }
                }
            }
        }
    }
}