package com.example.repository.model

import com.google.gson.annotations.SerializedName

data class NewsArticles(
    val id: Int = 0,

    /**
     * Name of the author for the article
     */
    val author: String? = null,

    /**
     * Title of the article
     */
    val title: String? = null,

    /**
     * Complete description of the article
     */
    val description: String? = null,

    /**
     * URL to the article
     */
    val url: String? = null,

    /**
     * URL of the artwork shown with article
     */
    val urlToImage: String? = null,

    /**
     * Date-time when the article was published
     */
    val publishedAt: String? = null
)