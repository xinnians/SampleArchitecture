package com.example.repository.api

import com.example.repository.model.LoginParam
import com.example.repository.model.LoginResponse
import com.example.repository.model.NewsSourceResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SampleService {
    @GET("articles?source=google-news&apiKey=ea919804ff5f4532a03c45e243a5b122")
    suspend fun getNewsFromGoogle(): NewsSourceResponse

    @POST("/api/Players/Login")
    suspend fun login(@Body param: LoginParam): LoginResponse
}