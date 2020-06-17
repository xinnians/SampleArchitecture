package com.example.repository

import androidx.lifecycle.LiveData
import com.example.repository.api.SampleService
import com.example.repository.model.*
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.*
import com.example.repository.room.Cart
import com.example.repository.room.LocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository(private val sampleService: SampleService, private val localDb: LocalDatabase) {

    companion object {
        const val test = "tttttt"
        const val isFakeMode = true
    }

    private var fakeGameMenuResponse: GameMenuResponse = GameMenuResponse(
        listOf(
//            GameMenuResponse.Data(
//                listOf(
//                    GameMenuResponse.Data.GameInfoEntity(220003, "新西兰45秒彩", 1, 2, -5),
//                    GameMenuResponse.Data.GameInfoEntity(220002, "腾讯分分彩", 1, 2, -10)
//                ), "Hot", 134
//            ), GameMenuResponse.Data(
//                listOf(
//                    GameMenuResponse.Data.GameInfoEntity(220003, "新西兰45秒彩", 1, 2, -5),
//                    GameMenuResponse.Data.GameInfoEntity(220002, "腾讯分分彩", 1, 2, -10)
//                ), "Favorite", 7
//            ),
            GameMenuResponse.Data(
                listOf(
                    GameMenuResponse.Data.GameInfoEntity(110001, "北京PK10", 1, 1, -60),
                    GameMenuResponse.Data.GameInfoEntity(110002, "幸运飞艇", 1, 1, -30)
                ), "北京賽車", 1
            ), GameMenuResponse.Data(
                listOf(
                    GameMenuResponse.Data.GameInfoEntity(220004, "俄罗斯1.5分彩", 1, 2, -10),
                    GameMenuResponse.Data.GameInfoEntity(220003, "新西兰45秒彩", 1, 2, -5),
                    GameMenuResponse.Data.GameInfoEntity(220002, "腾讯分分彩", 1, 2, -10),
                    GameMenuResponse.Data.GameInfoEntity(220001, "支付宝彩", 1, 2, -30),
                    GameMenuResponse.Data.GameInfoEntity(210004, "新疆时时彩", 1, 2, -120),
                    GameMenuResponse.Data.GameInfoEntity(210003, "天津时时彩", 1, 2, -120),
                    GameMenuResponse.Data.GameInfoEntity(210001, "欢乐生肖", 1, 2, -25)
                ), "时时彩", 2
            ), GameMenuResponse.Data(
                listOf(
                    GameMenuResponse.Data.GameInfoEntity(310003, "山东11选5", 1, 3, -60),
                    GameMenuResponse.Data.GameInfoEntity(310002, "广东11选5", 1, 3, -60),
                    GameMenuResponse.Data.GameInfoEntity(310001, "江西11选5", 1, 3, -60)
                ), "11选5", 3
            ), GameMenuResponse.Data(
                listOf(
                    GameMenuResponse.Data.GameInfoEntity(410005, "上海快三", 1, 4, -120),
                    GameMenuResponse.Data.GameInfoEntity(410004, "北京快三", 1, 4, -120),
                    GameMenuResponse.Data.GameInfoEntity(410003, "广西快三", 1, 4, -120),
                    GameMenuResponse.Data.GameInfoEntity(410002, "安徽快三", 1, 4, -120),
                    GameMenuResponse.Data.GameInfoEntity(410001, "江苏快三", 1, 4, -90)
                ), "快三", 4
            ), GameMenuResponse.Data(
                listOf(
                    GameMenuResponse.Data.GameInfoEntity(54000001, "六合彩60秒-01", 1, 5, -10),
                    GameMenuResponse.Data.GameInfoEntity(510001, "香港六合彩", 1, 5, -900)
                ), "六合彩", 5
            ), GameMenuResponse.Data(
                listOf(
                    GameMenuResponse.Data.GameInfoEntity(620001, "新西兰幸运28", 1, 6, -5),
                    GameMenuResponse.Data.GameInfoEntity(610001, "北京幸运28", 1, 6, -30)
                ), "幸运28", 6
            )
        )
    )

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
            delay(3000)
            val result = sampleService.getLoginResult(LoginRequest("test123", "test123"))
            emit(ViewState.success(result.data))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getGameMenu(token: String): Flow<ViewState<GameMenuResponse>> {
        return flow {
            emit(ViewState.loading())
            val result = if (isFakeMode) fakeGameMenuResponse else sampleService.getGameMenu(token)
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

    fun getLastIssueResult(token: String, gameId: ArrayList<Int>): Flow<ViewState<LastIssueResultResponse>> {
        return flow {
            emit(ViewState.loading())
            var queryParameter = ""
            gameId.forEach {
                queryParameter+=it
                queryParameter+=","
            }
            queryParameter = queryParameter.substring(0,queryParameter.length-1)
            val result = sampleService.lastIssueResult(token, queryParameter)
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

    fun getLotteryHistoricalRecord(token: String, gameId: Int, pageSize: Int = 10): Flow<ViewState<HistoricalResponse>> {
        return flow {
            emit(ViewState.loading())
            val result = sampleService.historical(token,gameId,pageSize)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getBetList(token: String,parameter: BetEntityParam): Flow<ViewState<BetListResponse>>{
        return flow {
            emit(ViewState.loading())
            val result = sampleService.betList(token,parameter)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    //Local Database
    fun addCart(cart: Cart): Flow<ViewState<Long>> {
        return flow {
            emit(ViewState.loading())
            val result = localDb.cartDao().addCart(cart)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getCartList(gameId: Int): Flow<ViewState<MutableList<Cart>>>{
        return flow {
            emit(ViewState.loading())
            val result = localDb.cartDao().getCartList(gameId)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getCartListArray(gameId: ArrayList<Int>): Flow<ViewState<MutableList<MutableList<Cart>>>>{
        return flow {
            emit(ViewState.loading())
            val result : MutableList<MutableList<Cart>> = mutableListOf()
            for (id in gameId){
                result.add(localDb.cartDao().getCartList(id))
            }
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }


    fun getAllGameId(): Flow<ViewState<MutableList<Int>>> {
        return flow {
            emit(ViewState.loading())
            val result = localDb.cartDao().getAllGameId()
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun delCart(cart: Cart): Flow<ViewState<Int>> {
        return flow {
            emit(ViewState.loading())
            val result = localDb.cartDao().delCart(cart)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun updateCart(cart: Cart): Flow<ViewState<Int>> {
        return flow {
            emit(ViewState.loading())
            val result = localDb.cartDao().updateCart(cart)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

}