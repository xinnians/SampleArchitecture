package com.example.page_bet.bet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.repository.Repository
import com.example.repository.model.NewsArticles
import com.example.repository.model.ViewState

class BetViewModel(repository: Repository) : ViewModel(){

    private val newsArticles: LiveData<ViewState<List<NewsArticles>>> = repository.getNewsArticles().asLiveData()

    /**
     * Return news articles to observeNotNull on the UI.
     */
    fun getNewsArticles(): LiveData<ViewState<List<NewsArticles>>> = newsArticles


}