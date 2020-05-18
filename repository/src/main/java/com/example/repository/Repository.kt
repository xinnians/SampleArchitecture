package com.example.repository

import com.example.repository.api.SampleService
import com.example.repository.model.NewsArticles
import com.example.repository.model.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository(private val sampleService: SampleService) {

    /**
     * Fetch the news articles from database if exist else fetch from web
     * and persist them in the database
     */
    fun getNewsArticles(): Flow<ViewState<List<NewsArticles>>> {
        return flow<ViewState<List<NewsArticles>>> {
            // 1. Start with loading + data from database
            emit(ViewState.loading())
//            emit(ViewState.success(newsDao.getNewsArticles()))

            // 2. Try fetching new news -> save + emit
            val newsSource = sampleService.getNewsFromGoogle()
//            newsDao.insertArticles(newsSource.articles)

            // 3. Get articles from database [Single source of truth]
//            emit(ViewState.success(newsDao.getNewsArticles()))
            emit(ViewState.success(newsSource.articles))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }
}