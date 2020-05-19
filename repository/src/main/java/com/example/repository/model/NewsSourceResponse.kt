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

data class NewsArticles(
    val id: Int = 0,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null
)