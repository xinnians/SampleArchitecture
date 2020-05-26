package com.example.repository.api

import com.example.repository.model.*
import retrofit2.http.*

interface SampleService {
    @GET("articles?source=google-news&apiKey=ea919804ff5f4532a03c45e243a5b122")
    suspend fun getNewsFromGoogle(): NewsSourceResponse

    /**
     * 登入
     * @param account String
     * @param password String
     * @param merchantId Int
     * */
    @POST("/api/Players/Login")
    suspend fun getLoginResult(@Body request: LoginRequest): LoginResponse

    /**
     * 遊戲Menu
     * @param token String
     * */
    @GET("api/Games/GameMenu")
    suspend fun getGameMenu(@Header("Authorization") token: String): GameMenuResponse
    /**
     * 取得當期期號資訊
     * @param token String
     * @param gameId int
     * */
    @GET("api/Issue/IssueInfo/{gameId}")
    suspend fun issueInfo(@Header("Authorization") token: String,
                  @Path("gameId") gameId: Int): IssueInfoResponse
    /**
     * 取得多個遊戲最新開獎結果
     * @param token String
     * @param gameId int
     * */
    @GET("/api/Draw/LatestIssueResult")
    suspend fun lastIssueResult(@Header("Authorization") token: String,
                          @Query("gameIdList") gameId: Int): LastIssueResultResponse

    /**
     * Mobile獎金盤-遊戲玩法清單資訊
     * @param gameId String
     * */
    @GET("/api/Games/PlayTypeInfoList/{gameId}")
    suspend fun playTypeInfoList(@Header("Authorization") token: String,
                         @Path("gameId") gameId: Int): PlayTypeInfoResponse
}