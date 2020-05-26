package com.example.page_bet.bet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.repository.Repository
import com.example.repository.model.*

class BetViewModel(var repository: Repository) : ViewModel(){

//    private val newsArticles: LiveData<ViewState<List<NewsArticles>>> = repository.getNewsArticles().asLiveData()
//
//    /**
//     * Return news articles to observeNotNull on the UI.
//     */
//    fun getNewsArticles(): LiveData<ViewState<List<NewsArticles>>> = newsArticles

    fun getCurrentIssueInfo(token:String, gameId: Int): LiveData<ViewState<IssueInfoResponse>>{
        return repository.getIssueInfo(token, gameId).asLiveData()
    }

    fun getLastIssueResult(token:String, gameId: Int): LiveData<ViewState<LastIssueResultResponse>>{
        return repository.getLastIssueResult(token, gameId).asLiveData()
    }

    fun getPlayTypeInfoList(token:String, gameId: Int): LiveData<ViewState<PlayTypeInfoResponse>>{
        return repository.getPlayTypeInfoList(token, gameId).asLiveData()
    }

}