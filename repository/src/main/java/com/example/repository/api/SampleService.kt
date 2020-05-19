package com.example.repository.api

import com.example.repository.model.GameMenuResponse
import com.example.repository.model.LoginRequest
import com.example.repository.model.LoginResponse
import com.example.repository.model.NewsSourceResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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
    suspend fun getGameMenu(@Header("Access-token") token: String): GameMenuResponse
}