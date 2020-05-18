package com.example.repository.api

import com.example.repository.model.NewsSourceResponse
import retrofit2.http.GET

interface SampleService {
    @GET("articles?source=google-news&apiKey=ea919804ff5f4532a03c45e243a5b122")
    suspend fun getNewsFromGoogle(): NewsSourceResponse
}