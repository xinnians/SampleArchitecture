package com.example.repository

import com.example.repository.api.SampleService
import com.example.repository.model.*
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.GameMenuResponse
import com.example.repository.model.bet.IssueInfoResponse
import com.example.repository.model.bet.LastIssueResultResponse
import com.example.repository.model.bet.PlayTypeInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository(private val sampleService: SampleService) {

    companion object {
        const val test = "tttttt"
    }

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

    fun getLoginResult(): Flow<ViewState<LoginResponse.Data>> {
        return flow {
            emit(ViewState.loading())
            val result = sampleService.getLoginResult(LoginRequest("test123","test123"))
            emit(ViewState.success(result.data))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getGameMenu(token: String): Flow<ViewState<GameMenuResponse>> {
        return flow {
            emit(ViewState.loading())
            val result = sampleService.getGameMenu(token)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getIssueInfo(token: String, gameId: Int): Flow<ViewState<IssueInfoResponse>> {
        return flow {
            emit(ViewState.loading())
            val result = sampleService.issueInfo(token, gameId)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getLastIssueResult(token: String, gameId: Int): Flow<ViewState<LastIssueResultResponse>> {
        return flow {
            emit(ViewState.loading())
            val result = sampleService.lastIssueResult(token, gameId)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getPlayTypeInfoList(token: String, gameId: Int): Flow<ViewState<PlayTypeInfoResponse>> {
        return flow {
            emit(ViewState.loading())
            val result = sampleService.playTypeInfoList(token, gameId)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }
}