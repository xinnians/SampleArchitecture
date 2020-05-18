package com.example.repository.model

import com.google.gson.annotations.SerializedName

data class NewsSourceResponse(
    @SerializedName("status")
    val status: String = "",

    @SerializedName("source")
    val source: String = "",

    @SerializedName("sortBy")
    val sortBy: String = "",

    @SerializedName("articles")
    val articles: List<NewsArticles> = emptyList()
)