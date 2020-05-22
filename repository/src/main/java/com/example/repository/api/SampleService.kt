package com.example.repository.api

import com.example.repository.model.*
import retrofit2.Call
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
    fun issueInfo(@Header("Access-token") token: String,
                  @Path("gameId") gameId: Int): IssueInfoResponse
}