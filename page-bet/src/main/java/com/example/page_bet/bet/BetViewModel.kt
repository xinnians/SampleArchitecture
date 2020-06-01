package com.example.page_bet.bet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.repository.Repository
import com.example.repository.model.base.ViewState
import com.example.repository.model.base.ViewState.Success
import com.example.repository.model.bet.BetTypeEntity
import com.example.repository.model.bet.IssueInfoResponse
import com.example.repository.model.bet.LastIssueResultResponse
import com.example.repository.model.bet.PlayTypeInfoResponse
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class BetViewModel(var repository: Repository) : ViewModel(){

    var playTypeInfoList: PlayTypeInfoResponse? = null

//    private val newsArticles: LiveData<ViewState<List<NewsArticles>>> = repository.getNewsArticles().asLiveData()
//
//    /**
//     * Return news articles to observeNotNull on the UI.
//     */
//    fun getNewsArticles(): LiveData<ViewState<List<NewsArticles>>> = newsArticles

//    private var playTypeInfoList: LiveData<List<BetTypeGroups>> =

    fun getCurrentIssueInfo(token:String, gameId: Int): LiveData<ViewState<IssueInfoResponse>>{
        return repository.getIssueInfo(token, gameId).asLiveData()
    }

    fun getLastIssueResult(token:String, gameId: Int): LiveData<ViewState<LastIssueResultResponse>>{
        return repository.getLastIssueResult(token, gameId).asLiveData()
    }

    fun getPlayTypeInfoList(token:String, gameId: Int): LiveData<ViewState<List<BetTypeEntity>>>{
        return repository.getPlayTypeInfoList(token, gameId).flatMapConcat{ state ->
            flow<ViewState<List<BetTypeEntity>>> {
                when (state) {
                    is Success -> {
                        playTypeInfoList = state.data
                        var betTypeList: ArrayList<BetTypeEntity> = arrayListOf()
                        if(!state.data.data.betTypeGroupList.isNullOrEmpty()){
                            for (item in state.data.data.betTypeGroupList) {
                                betTypeList.addAll(item.betTypeEntityList)
                            }
                        }
                        emit(ViewState.success(betTypeList.toList()))
                    }
                    is ViewState.Loading -> {
                        emit(ViewState.loading())
                    }
                    is ViewState.Error -> {
                        emit(ViewState.error(state.message.orEmpty()))
                    }
                }
            }
        }.asLiveData()
    }

}