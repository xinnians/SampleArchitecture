package com.example.repository

import com.example.repository.api.SampleService
import com.example.repository.model.*
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.*
import com.example.repository.room.Cart
import com.example.repository.room.LocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository(private val sampleService: SampleService, private val localDb: LocalDatabase) {

    companion object {
        const val isFakeMode = true
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
            val result = if (isFakeMode) fakeIssueInfoResponse else sampleService.issueInfo(token, gameId)
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
                queryParameter += it
                queryParameter += ","
            }
            queryParameter = queryParameter.substring(0, queryParameter.length - 1)
            val result = if (isFakeMode) fakeLastIssueResultResponse(gameId) else sampleService.lastIssueResult(token, queryParameter)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getPlayTypeInfoList(token: String, gameId: Int): Flow<ViewState<PlayTypeInfoResponse>> {
        return flow {
            emit(ViewState.loading())
            val result = if (isFakeMode) fakePlayTypeInfoResponse else sampleService.playTypeInfoList(token, gameId)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getLotteryHistoricalRecord(token: String, gameId: Int, pageSize: Int = 10): Flow<ViewState<HistoricalResponse>> {
        return flow {
            emit(ViewState.loading())
            val result = sampleService.historical(token, gameId, pageSize)
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getBetList(token: String, parameter: BetEntityParam): Flow<ViewState<BetListResponse>> {
        return flow {
            emit(ViewState.loading())
            val result = sampleService.betList(token, parameter)
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

    fun getCartList(gameId: Int): Flow<ViewState<MutableList<Cart>>> {
        return flow {
            emit(ViewState.loading())
            emit(ViewState.success(localDb.cartDao().getCartList(gameId)))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getCartListArray(gameId: ArrayList<Int>): Flow<ViewState<MutableList<MutableList<Cart>>>> {
        return flow {
            emit(ViewState.loading())
            val result: MutableList<MutableList<Cart>> = mutableListOf()
            for (id in gameId) {
                result.add(localDb.cartDao().getCartList(id))
            }
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getAllCartList(): Flow<ViewState<MutableList<Cart>>> {
        return flow {
            emit(ViewState.loading())
            val result = localDb.cartDao().getAllCartList()
            emit(ViewState.success(result))
        }.catch {
            emit(ViewState.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)
    }

    fun getAllGameId(): Flow<ViewState<MutableList<Int>>> {
        return flow {
            emit(ViewState.loading())
            var result: MutableList<Int> = mutableListOf()
            result = localDb.cartDao().getAllGameId()
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

    fun delCartById(gameId: Int): Flow<ViewState<Int>> {
        return flow {
            emit(ViewState.loading())
            val result = localDb.cartDao().delCartById(gameId)
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

    private var fakeGameMenuResponse: GameMenuResponse =
        GameMenuResponse(listOf(GameMenuResponse.Data(listOf(GameMenuResponse.Data.GameInfoEntity(220003, "新西兰45秒彩", 1, 2, -5),
                                                             GameMenuResponse.Data.GameInfoEntity(220002, "腾讯分分彩", 1, 2, -10)), "Hot", 134),
                                GameMenuResponse.Data(listOf(GameMenuResponse.Data.GameInfoEntity(220003, "新西兰45秒彩", 1, 2, -5),
                                                             GameMenuResponse.Data.GameInfoEntity(220002, "腾讯分分彩", 1, 2, -10)), "Favorite", 7),
                                GameMenuResponse.Data(listOf(GameMenuResponse.Data.GameInfoEntity(110001, "北京PK10", 1, 1, -60),
                                                             GameMenuResponse.Data.GameInfoEntity(110002, "幸运飞艇", 1, 1, -30)), "北京賽車", 1),
                                GameMenuResponse.Data(listOf(GameMenuResponse.Data.GameInfoEntity(220004, "俄罗斯1.5分彩", 1, 2, -10),
                                                             GameMenuResponse.Data.GameInfoEntity(220003, "新西兰45秒彩", 1, 2, -5),
                                                             GameMenuResponse.Data.GameInfoEntity(220002, "腾讯分分彩", 1, 2, -10),
                                                             GameMenuResponse.Data.GameInfoEntity(220001, "支付宝彩", 1, 2, -30),
                                                             GameMenuResponse.Data.GameInfoEntity(210004, "新疆时时彩", 1, 2, -120),
                                                             GameMenuResponse.Data.GameInfoEntity(210003, "天津时时彩", 1, 2, -120),
                                                             GameMenuResponse.Data.GameInfoEntity(210001, "欢乐生肖", 1, 2, -25)), "时时彩", 2),
                                GameMenuResponse.Data(listOf(GameMenuResponse.Data.GameInfoEntity(310003, "山东11选5", 1, 3, -60),
                                                             GameMenuResponse.Data.GameInfoEntity(310002, "广东11选5", 1, 3, -60),
                                                             GameMenuResponse.Data.GameInfoEntity(310001, "江西11选5", 1, 3, -60)), "11选5", 3),
                                GameMenuResponse.Data(listOf(GameMenuResponse.Data.GameInfoEntity(410005, "上海快三", 1, 4, -120),
                                                             GameMenuResponse.Data.GameInfoEntity(410004, "北京快三", 1, 4, -120),
                                                             GameMenuResponse.Data.GameInfoEntity(410003, "广西快三", 1, 4, -120),
                                                             GameMenuResponse.Data.GameInfoEntity(410002, "安徽快三", 1, 4, -120),
                                                             GameMenuResponse.Data.GameInfoEntity(410001, "江苏快三", 1, 4, -90)), "快三", 4),
                                GameMenuResponse.Data(listOf(GameMenuResponse.Data.GameInfoEntity(54000001, "六合彩60秒-01", 1, 5, -10),
                                                             GameMenuResponse.Data.GameInfoEntity(510001, "香港六合彩", 1, 5, -900)), "六合彩", 5),
                                GameMenuResponse.Data(listOf(GameMenuResponse.Data.GameInfoEntity(620001, "新西兰幸运28", 1, 6, -5),
                                                             GameMenuResponse.Data.GameInfoEntity(610001, "北京幸运28", 1, 6, -30)), "幸运28", 6)))

    private var fakeLoginResponse: LoginResponse =
        LoginResponse(data = LoginResponse.Data(0, 0L, "fake", "fake"), message = "isFake", messageCode = 0, status = 0)

    private var fakeIssueInfoResponse: IssueInfoResponse =
        IssueInfoResponse(IssueInfoResponse.Data(1592806090000L, 1592806095000L, 3692692, "202006221451", 1592806074431L))

    private var fakeLastIssueResultResponse = { gameId: ArrayList<Int> ->
        var list: ArrayList<LastIssueResultResponse.Data> = arrayListOf()
        gameId.forEach {
            list.add(LastIssueResultResponse.Data(gameId = it, issueNum = "202006221450", winNum = fakeWinNum(it)))
        }
        LastIssueResultResponse(list, "isFake", 0, 0)
    }

    private var fakeWinNum = { gameId: Int ->
        when (gameId) {
            110001, 110002 -> "${(0..10).random()},${(0..10).random()},${(0..10).random()},${(0..10).random()},${(0..10).random()},${(0..10).random()},${(0..10).random()},${(0..10).random()},${(0..10).random()},${(0..10).random()}"
            220004, 220003, 220002, 220001, 210004, 210003, 210001 -> "${(0..9).random()},${(0..9).random()},${(0..9).random()},${(0..9).random()},${(0..9).random()}"
            310003, 310002, 310001 -> "${(0..11).random()},${(0..11).random()},${(0..11).random()},${(0..11).random()},${(0..11).random()}"
            410005, 410004, 410003, 410002, 410001 -> "${(0..9).random()},${(0..9).random()},${(0..9).random()}"
            54000001, 510001 -> "${(1..40).random()},${(1..40).random()},${(1..40).random()},${(1..40).random()},${(1..40).random()},${(1..40).random()},${(1..40).random()}"
            620001, 610001 -> "${(0..9).random()},${(0..9).random()},${(0..9).random()}"
            else -> "1,2,3,4,5"
        }
    }

    private var fakePlayTypeInfoResponse: PlayTypeInfoResponse = PlayTypeInfoResponse(Data(1000000,
                                                                                           0,
                                                                                           listOf(BetTypeGroups("常规玩法",
                                                                                                                listOf(BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 50,
                                                                                                                                     betTypeDisplayName = "五星",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             5001,
                                                                                                                                             "五星直选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选复式",
                                                                                                                                                     "投注内容：1，2，3，4，5 开奖号码：1，2，3，4，5 中奖 ",
                                                                                                                                                     true,
                                                                                                                                                     "所选号码的万位、千位、百位、十位、个位与开奖号码相同，且顺序一致，即为中奖。",
                                                                                                                                                     listOf(),
                                                                                                                                                     205000,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B4%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选单式",
                                                                                                                                                     "投注内容：1，2，3，4，5 开奖号码：1，2，3，4，5 中奖 ",
                                                                                                                                                     true,
                                                                                                                                                     "所选号码的万位、千位、百位、十位、个位与开奖号码相同，且顺序一致，即为中奖。",
                                                                                                                                                     listOf(),
                                                                                                                                                     205001,
                                                                                                                                                     "%5E%5Cd%7B5%7D(%5C,%5Cd%7B5%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选组合",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205010,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B4%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             5002,
                                                                                                                                             "五星组选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选120",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205021,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B5,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选60",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205022,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B3,10%7D)%7B1%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选30",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205023,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B1%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选20",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205024,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B2,10%7D)%7B1%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选10",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205025,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B1%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选5",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205026,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B1%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             5003,
                                                                                                                                             "五星特殊",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "一帆风顺",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205051,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "好事成双",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205052,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "三星报喜",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205053,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "四季发财",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205054,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))),
                                                                                                                       BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 40,
                                                                                                                                     betTypeDisplayName = "四星",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             4001,
                                                                                                                                             "四星直选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前四复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204100,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B3%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后四复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204200,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B3%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前四单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204101,
                                                                                                                                                     "%5E%5Cd%7B4%7D(%5C,%5Cd%7B4%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后四单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204201,
                                                                                                                                                     "%5E%5Cd%7B4%7D(%5C,%5Cd%7B4%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前四组合",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204110,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B3%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后四组合",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204210,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B3%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             4002,
                                                                                                                                             "四星组选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前四组选24",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204121,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B4,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后四组选24",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204221,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B4,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前四组选12",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204122,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B2,10%7D)%7B1%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后四组选12",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204222,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B2,10%7D)%7B1%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前四组选6",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204123,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后四组选6",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204223,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前四组选4",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204124,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B1%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后四组选4",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204224,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B1%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))),
                                                                                                                       BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 31,
                                                                                                                                     betTypeDisplayName = "前三",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             3101,
                                                                                                                                             "前三直选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203100,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B2%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203101,
                                                                                                                                                     "%5E%5Cd%7B3%7D(%5C,%5Cd%7B3%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203102,
                                                                                                                                                     "%5E([0-9]%7C[1][0-9]%7C[2][0-7])(%5C,([0-9]%7C[1][0-9]%7C[2][0-7]))%7B0,27%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选跨度",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203103,
                                                                                                                                                     "%5E(?!%5Cd*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)%5Cd(?:,%5Cd)%7B0,9%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选组合",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203110,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B2%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             3102,
                                                                                                                                             "前三组选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组三复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203123,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组三单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203121,
                                                                                                                                                     "%5E(?:(?:(%5Cd)%5C1(?:(?!%5C1)%5Cd))%7C(?:(%5Cd)((?!%5C2)%5Cd)%5C3)%7C(?:(%5Cd)(?:(?!%5C4)%5Cd)%5C4))(?:%5C,(?:(?:(%5Cd)%5C5(?:(?!%5C5)%5Cd))%7C(?:(%5Cd)((?!%5C6)%5Cd)%5C7)%7C(?:(%5Cd)(?:(?!%5C8)%5Cd)%5C8)))%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组六复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203124,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B3,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组六单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203122,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B3%7D(?:%5C,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B3%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203131,
                                                                                                                                                     "%5E([1-9]%7C[1][0-9]%7C[2][0-6])(%5C,([1-9]%7C[1][0-9]%7C[2][0-6]))%7B0,25%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选包胆",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203133,
                                                                                                                                                     "%5E(?!(?:(?:%5E%7C,)%5Cd)*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)%5Cd(?:,%5Cd)%7B0,9%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             3103,
                                                                                                                                             "前三其他",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "和值尾数",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203134,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "特殊号码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203135,
                                                                                                                                                     "%5E[1-3](,[1-3])%7B0,2%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))),
                                                                                                                       BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 32,
                                                                                                                                     betTypeDisplayName = "中三",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             3201,
                                                                                                                                             "中三直选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203200,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B2%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203201,
                                                                                                                                                     "%5E%5Cd%7B3%7D(%5C,%5Cd%7B3%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203202,
                                                                                                                                                     "%5E([0-9]%7C[1][0-9]%7C[2][0-7])(%5C,([0-9]%7C[1][0-9]%7C[2][0-7]))%7B0,27%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选跨度",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203203,
                                                                                                                                                     "%5E(?!%5Cd*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)%5Cd(?:,%5Cd)%7B0,9%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选组合",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203210,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B2%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             3202,
                                                                                                                                             "中三組选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组三复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203223,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组三单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203221,
                                                                                                                                                     "%5E(?:(?:(%5Cd)%5C1(?:(?!%5C1)%5Cd))%7C(?:(%5Cd)((?!%5C2)%5Cd)%5C3)%7C(?:(%5Cd)(?:(?!%5C4)%5Cd)%5C4))(?:%5C,(?:(?:(%5Cd)%5C5(?:(?!%5C5)%5Cd))%7C(?:(%5Cd)((?!%5C6)%5Cd)%5C7)%7C(?:(%5Cd)(?:(?!%5C8)%5Cd)%5C8)))%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组六复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203224,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B3,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组六单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203222,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B3%7D(?:%5C,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B3%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203231,
                                                                                                                                                     "%5E([1-9]%7C[1][0-9]%7C[2][0-6])(%5C,([1-9]%7C[1][0-9]%7C[2][0-6]))%7B0,25%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选包胆",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203233,
                                                                                                                                                     "%5E(?!(?:(?:%5E%7C,)%5Cd)*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)%5Cd(?:,%5Cd)%7B0,9%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             3203,
                                                                                                                                             "中三其他",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "和值尾数",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203234,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "特殊号码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203235,
                                                                                                                                                     "%5E[1-3](,[1-3])%7B0,2%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))),
                                                                                                                       BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 33,
                                                                                                                                     betTypeDisplayName = "后三",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             3301,
                                                                                                                                             "后三直选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203300,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B2%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203301,
                                                                                                                                                     "%5E%5Cd%7B3%7D(%5C,%5Cd%7B3%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203302,
                                                                                                                                                     "%5E([0-9]%7C[1][0-9]%7C[2][0-7])(%5C,([0-9]%7C[1][0-9]%7C[2][0-7]))%7B0,27%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选跨度",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203303,
                                                                                                                                                     "%5E(?!%5Cd*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)%5Cd(?:,%5Cd)%7B0,9%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选组合",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203310,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B2%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             3302,
                                                                                                                                             "后三組选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组三复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203323,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组三单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203321,
                                                                                                                                                     "%5E(?:(?:(%5Cd)%5C1(?:(?!%5C1)%5Cd))%7C(?:(%5Cd)((?!%5C2)%5Cd)%5C3)%7C(?:(%5Cd)(?:(?!%5C4)%5Cd)%5C4))(?:%5C,(?:(?:(%5Cd)%5C5(?:(?!%5C5)%5Cd))%7C(?:(%5Cd)((?!%5C6)%5Cd)%5C7)%7C(?:(%5Cd)(?:(?!%5C8)%5Cd)%5C8)))%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组六复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203324,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B3,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组六单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203322,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B3%7D(?:%5C,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B3%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203331,
                                                                                                                                                     "%5E([1-9]%7C[1][0-9]%7C[2][0-6])(%5C,([1-9]%7C[1][0-9]%7C[2][0-6]))%7B0,25%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选包胆",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203333,
                                                                                                                                                     "%5E(?!(?:(?:%5E%7C,)%5Cd)*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)%5Cd(?:,%5Cd)%7B0,9%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             3303,
                                                                                                                                             "后三其他",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "和值尾数",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203334,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "特殊号码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203335,
                                                                                                                                                     "%5E[1-3](,[1-3])%7B0,2%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))),
                                                                                                                       BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 21,
                                                                                                                                     betTypeDisplayName = "前二",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             2101,
                                                                                                                                             "前二直选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202100,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B1%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202101,
                                                                                                                                                     "%5E%5Cd%7B2%7D(%5C,%5Cd%7B2%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202102,
                                                                                                                                                     "%5E([0-9]%7C[1][0-8])(%5C,([0-9]%7C[1][0-8]))%7B0,18%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选跨度",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202103,
                                                                                                                                                     "%5E(?!%5Cd*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)%5Cd(?:,%5Cd)%7B0,9%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             2102,
                                                                                                                                             "前二組选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202110,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202120,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2%7D(?:%5C,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B2%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202131,
                                                                                                                                                     "%5E([1-9]%7C[1][0-7])(%5C,([1-9]%7C[1][0-7]))%7B0,16%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选包胆",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202133,
                                                                                                                                                     "%5E(?!(?:(?:%5E%7C,)%5Cd)*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)%5Cd(?:,%5Cd)%7B0,9%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))),
                                                                                                                       BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 22,
                                                                                                                                     betTypeDisplayName = "后二",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             2201,
                                                                                                                                             "后二直选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202200,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B1%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202201,
                                                                                                                                                     "%5E%5Cd%7B2%7D(%5C,%5Cd%7B2%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202202,
                                                                                                                                                     "%5E([0-9]%7C[1][0-8])(%5C,([0-9]%7C[1][0-8]))%7B0,18%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选跨度",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202203,
                                                                                                                                                     "%5E(?!%5Cd*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)%5Cd(?:,%5Cd)%7B0,9%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             2202,
                                                                                                                                             "后二組选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202210,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202220,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2%7D(?:%5C,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B2%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202231,
                                                                                                                                                     "%5E([1-9]%7C[1][0-7])(%5C,([1-9]%7C[1][0-7]))%7B0,16%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选包胆",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202233,
                                                                                                                                                     "%5E(?!(?:(?:%5E%7C,)%5Cd)*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)%5Cd(?:,%5Cd)%7B0,9%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))),
                                                                                                                       BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 81,
                                                                                                                                     betTypeDisplayName = "定位胆",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             8181,
                                                                                                                                             "定位胆",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "五星定位胆",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205040,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B0,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B0,10%7D)%7B4%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))),
                                                                                                                       BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 82,
                                                                                                                                     betTypeDisplayName = "不定位",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             8203,
                                                                                                                                             "三星",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前三一码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203140,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前三二码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203142,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "中三一码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203240,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "中三二码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203242,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后三一码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203340,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后三二码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203342,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             8204,
                                                                                                                                             "四星",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前四一码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204141,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前四二码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204142,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后四一码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204241,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后四二码",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204242,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             8205,
                                                                                                                                             "五星",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "二码不定",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205042,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "三码不定",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     205043,
                                                                                                                                                     "%5E(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B3,10%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))),
                                                                                                                       BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 70,
                                                                                                                                     betTypeDisplayName = "大小单双",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             7070,
                                                                                                                                             "大小单双",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前二",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202150,
                                                                                                                                                     "%5E(?![1-4]*?([1-4])[1-4]*?%5C1)[1-4]%7B1,4%7D(?:,(?![1-4]*?([1-4])[1-4]*?%5C2)[1-4]%7B1,4%7D)%7B1%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后二",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202250,
                                                                                                                                                     "%5E(?![1-4]*?([1-4])[1-4]*?%5C1)[1-4]%7B1,4%7D(?:,(?![1-4]*?([1-4])[1-4]*?%5C2)[1-4]%7B1,4%7D)%7B1%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "前三",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203150,
                                                                                                                                                     "%5E(?![1-4]*?([1-4])[1-4]*?%5C1)[1-4]%7B1,4%7D(?:,(?![1-4]*?([1-4])[1-4]*?%5C2)[1-4]%7B1,4%7D)%7B2%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "中三",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203250,
                                                                                                                                                     "%5E(?![1-4]*?([1-4])[1-4]*?%5C1)[1-4]%7B1,4%7D(?:,(?![1-4]*?([1-4])[1-4]*?%5C2)[1-4]%7B1,4%7D)%7B2%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "后三",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203350,
                                                                                                                                                     "%5E(?![1-4]*?([1-4])[1-4]*?%5C1)[1-4]%7B1,4%7D(?:,(?![1-4]*?([1-4])[1-4]*?%5C2)[1-4]%7B1,4%7D)%7B2%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))),
                                                                                                                       BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 60,
                                                                                                                                     betTypeDisplayName = "龙虎和",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             6060,
                                                                                                                                             "龙虎和",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "龙虎和",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     206010,
                                                                                                                                                     "%5E(?!(?:(?:%5E%7C,)%5Cd)*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)%5Cd(?:,%5Cd)%7B0,9%7D@(?!%5Cd*?(?:%7C,)(%5Cd)(?:,%5Cd)*?,%5C2)[1-3](?:,[1-3])%7B0,2%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1)))

                                                                                                                )),
                                                                                                  BetTypeGroups("任选玩法",
                                                                                                                listOf(BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 92,
                                                                                                                                     betTypeDisplayName = "任选二",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             9201,
                                                                                                                                             "任二直选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202000,
                                                                                                                                                     "%5E(?:(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C3)%5Cd%7B0,10%7D)%7B3%7D%7C(?!%5Cd*?(%5Cd)%5Cd*?%5C4)%5Cd%7B1,10%7D,,(?!%5Cd*?(%5Cd)%5Cd*?%5C5)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C6)%5Cd%7B0,10%7D)%7B2%7D%7C(?!%5Cd*?(%5Cd)%5Cd*?%5C7)%5Cd%7B1,10%7D,,,(?!%5Cd*?(%5Cd)%5Cd*?%5C8)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C9)%5Cd%7B0,10%7D)%7C(?!%5Cd*?(%5Cd)%5Cd*?%5C10)%5Cd%7B1,10%7D,,,,(?!%5Cd*?(%5Cd)%5Cd*?%5C11)%5Cd%7B1,10%7D%7C(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C12)%5Cd%7B1,10%7D)%7B2%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C13)%5Cd%7B0,10%7D)%7B2%7D%7C,(?!%5Cd*?(%5Cd)%5Cd*?%5C14)%5Cd%7B1,10%7D,,(?!%5Cd*?(%5Cd)%5Cd*?%5C15)%5Cd%7B1,10%7D,(?!%5Cd*?(%5Cd)%5Cd*?%5C16)%5Cd%7B0,10%7D%7C,(?!%5Cd*?(%5Cd)%5Cd*?%5C17)%5Cd%7B1,10%7D,,,(?!%5Cd*?(%5Cd)%5Cd*?%5C18)%5Cd%7B1,10%7D%7C,(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C19)%5Cd%7B1,10%7D)%7B2%7D,(?!%5Cd*?(%5Cd)%5Cd*?%5C20)%5Cd%7B0,10%7D%7C,,(?!%5Cd*?(%5Cd)%5Cd*?%5C21)%5Cd%7B1,10%7D,,(?!%5Cd*?(%5Cd)%5Cd*?%5C22)%5Cd%7B1,10%7D%7C,,(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C23)%5Cd%7B1,10%7D)%7B2%7D)\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202001,
                                                                                                                                                     "%5E(?!(?:(?:%5E%7C,)%5Cd)*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)[0-4](?:,[0-4])%7B1,4%7D@%5Cd%7B2%7D(?:,%5Cd%7B2%7D)*\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202002,
                                                                                                                                                     "%5E([0-4](,[0-4])%7B1,4%7D)@([0-9]%7C[1][0-8])(%5C,([0-9]%7C[1][0-8]))%7B0,18%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             9202,
                                                                                                                                             "任二組选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202010,
                                                                                                                                                     "%5E([0-4](,[0-4])%7B1,4%7D)@(?!%5Cd*?(%5Cd)%5Cd*?%5C3)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202020,
                                                                                                                                                     "%5E([0-4])(?:,(?!%5C1)([0-4]))(?:,(?!%5C1%7C%5C2)([0-4]))?(?:,(?!%5C1%7C%5C2%7C%5C3)([0-4]))?(?:,(?!%5C1%7C%5C2%7C%5C3%7C%5C4)([0-4]))?@(?!%5Cd*?(%5Cd)%5Cd*?%5C6)%5Cd%7B2%7D(?:%5C,(?!%5Cd*?(%5Cd)%5Cd*?%5C7)%5Cd%7B2%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     202031,
                                                                                                                                                     "%5E([0-4](,[0-4])%7B1,4%7D)@([1-9]%7C[1][0-7])(%5C,([1-9]%7C[1][0-7]))%7B0,16%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))),
                                                                                                                       BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 93,
                                                                                                                                     betTypeDisplayName = "任选三",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             9301,
                                                                                                                                             "任三直选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203000,
                                                                                                                                                     "%5E(?:(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D)%7B2%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C3)%5Cd%7B0,10%7D)%7B2%7D%7C(?!%5Cd*?(%5Cd)%5Cd*?%5C4)%5Cd%7B1,10%7D,(?!%5Cd*?(%5Cd)%5Cd*?%5C5)%5Cd%7B1,10%7D,,(?!%5Cd*?(%5Cd)%5Cd*?%5C6)%5Cd%7B1,10%7D,(?!%5Cd*?(%5Cd)%5Cd*?%5C7)%5Cd%7B0,10%7D%7C(?!%5Cd*?(%5Cd)%5Cd*?%5C8)%5Cd%7B1,10%7D,(?!%5Cd*?(%5Cd)%5Cd*?%5C9)%5Cd%7B1,10%7D,,,(?!%5Cd*?(%5Cd)%5Cd*?%5C10)%5Cd%7B1,10%7D%7C(?!%5Cd*?(%5Cd)%5Cd*?%5C11)%5Cd%7B1,10%7D,(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C12)%5Cd%7B1,10%7D)%7B2%7D,(?!%5Cd*?(%5Cd)%5Cd*?%5C13)%5Cd%7B0,10%7D%7C(?!%5Cd*?(%5Cd)%5Cd*?%5C14)%5Cd%7B1,10%7D(?:,,(?!%5Cd*?(%5Cd)%5Cd*?%5C15)%5Cd%7B1,10%7D)%7B2%7D%7C(?!%5Cd*?(%5Cd)%5Cd*?%5C16)%5Cd%7B1,10%7D,,(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C17)%5Cd%7B1,10%7D)%7B2%7D%7C(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C18)%5Cd%7B1,10%7D)%7B3%7D,(?!%5Cd*?(%5Cd)%5Cd*?%5C19)%5Cd%7B0,10%7D%7C(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C20)%5Cd%7B1,10%7D)%7B2%7D,,(?!%5Cd*?(%5Cd)%5Cd*?%5C21)%5Cd%7B1,10%7D%7C,(?!%5Cd*?(%5Cd)%5Cd*?%5C22)%5Cd%7B1,10%7D,(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C23)%5Cd%7B1,10%7D)%7B2%7D%7C,(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C24)%5Cd%7B1,10%7D)%7B3%7D)\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203001,
                                                                                                                                                     "%5E(?!(?:(?:%5E%7C,)%5Cd)*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)[0-4](?:,[0-4])%7B2,4%7D@%5Cd%7B3%7D(?:,%5Cd%7B3%7D)*\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203002,
                                                                                                                                                     "%5E(?!(?:(?:%5E%7C,)%5Cd)*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)[0-4](?:,[0-4])%7B2,4%7D@(?:%5Cd%7C1%5Cd%7C2[0-7])(?:,(?:%5Cd%7C1%5Cd%7C2[0-7]))%7B0,27%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             9302,
                                                                                                                                             "任三組选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组三复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203023,
                                                                                                                                                     "%5E([0-4](,[0-4])%7B2,4%7D)@(?!%5Cd*?(%5Cd)%5Cd*?%5C3)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组三单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203021,
                                                                                                                                                     "%5E(?:[0-4](?:,[0-4])%7B2,4%7D)@(?:(?:(%5Cd)%5C1(?:(?!%5C1)%5Cd))%7C(?:(%5Cd)((?!%5C2)%5Cd)%5C3)%7C(?:(%5Cd)(?:(?!%5C4)%5Cd)%5C4))(?:%5C,(?:(?:(%5Cd)%5C5(?:(?!%5C5)%5Cd))%7C(?:(%5Cd)((?!%5C6)%5Cd)%5C7)%7C(?:(%5Cd)(?:(?!%5C8)%5Cd)%5C8)))%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组六复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203024,
                                                                                                                                                     "%5E([0-4](,[0-4])%7B2,4%7D)@(?!%5Cd*?(%5Cd)%5Cd*?%5C3)%5Cd%7B3,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组六单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203022,
                                                                                                                                                     "%5E(?:[0-4](?:,[0-4])%7B2,4%7D)@(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B3%7D(?:%5C,(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B3%7D)%7B0,%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选和值",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     203031,
                                                                                                                                                     "%5E([0-4](,[0-4])%7B2,4%7D)@([1-9]%7C[1][0-9]%7C[2][0-6])(%5C,([1-9]%7C[1][0-9]%7C[2][0-6]))%7B0,25%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))),
                                                                                                                       BetTypeEntity(status = 1,
                                                                                                                                     betTypeCode = 94,
                                                                                                                                     betTypeDisplayName = "任选四",
                                                                                                                                     mobileBetGroupEntityList = listOf(
                                                                                                                                         BetGroupEntity(
                                                                                                                                             9401,
                                                                                                                                             "任四直选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选复式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204000,
                                                                                                                                                     "%5E(?:(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C1)%5Cd%7B1,10%7D)%7B4%7D%7C(?!%5Cd*?(%5Cd)%5Cd*?%5C2)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C3)%5Cd%7B1,10%7D)%7B3%7D,(?!%5Cd*?(%5Cd)%5Cd*?%5C4)%5Cd%7B0,10%7D%7C(?!%5Cd*?(%5Cd)%5Cd*?%5C4)%5Cd%7B1,10%7D,(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C5)%5Cd%7B1,10%7D)%7B3%7D%7C(?!%5Cd*?(%5Cd)%5Cd*?%5C6)%5Cd%7B1,10%7D,(?!%5Cd*?(%5Cd)%5Cd*?%5C7)%5Cd%7B1,10%7D,(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C8)%5Cd%7B1,10%7D)%7B2%7D%7C(?!%5Cd*?(%5Cd)%5Cd*?%5C9)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C10)%5Cd%7B1,10%7D)%7B2%7D,,(?!%5Cd*?(%5Cd)%5Cd*?%5C11)%5Cd%7B1,10%7D)\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "直选单式",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204001,
                                                                                                                                                     "%5E(?!(?:(?:%5E%7C,)%5Cd)*?(?:%5E%7C,)(%5Cd)(?:,%5Cd)*?,%5C1)[0-4](?:,[0-4])%7B3,4%7D@%5Cd%7B4%7D(?:,%5Cd%7B4%7D)*\$",
                                                                                                                                                     1)),
                                                                                                                                             1),
                                                                                                                                         BetGroupEntity(
                                                                                                                                             9402,
                                                                                                                                             "任四組选",
                                                                                                                                             listOf(
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选24",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204021,
                                                                                                                                                     "%5E([0-4](,[0-4])%7B3,4%7D)@(?!%5Cd*?(%5Cd)%5Cd*?%5C3)%5Cd%7B4,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选12",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204022,
                                                                                                                                                     "%5E([0-4](,[0-4])%7B3,4%7D)@(?!%5Cd*?(%5Cd)%5Cd*?%5C3)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C4)%5Cd%7B2,10%7D)%7B1%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选6",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204023,
                                                                                                                                                     "%5E([0-4](,[0-4])%7B3,4%7D)@(?!%5Cd*?(%5Cd)%5Cd*?%5C3)%5Cd%7B2,10%7D\$",
                                                                                                                                                     1),
                                                                                                                                                 PlayTypeInfoEntity(
                                                                                                                                                     "组选4",
                                                                                                                                                     "",
                                                                                                                                                     true,
                                                                                                                                                     "",
                                                                                                                                                     listOf(),
                                                                                                                                                     204024,
                                                                                                                                                     "%5E([0-4](,[0-4])%7B3,4%7D)@(?!%5Cd*?(%5Cd)%5Cd*?%5C3)%5Cd%7B1,10%7D(?:,(?!%5Cd*?(%5Cd)%5Cd*?%5C4)%5Cd%7B1,10%7D)%7B1%7D\$",
                                                                                                                                                     1)),
                                                                                                                                             1))))))))


}