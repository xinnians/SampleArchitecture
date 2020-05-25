package com.example.repository.model


import com.google.gson.annotations.SerializedName

data class LastIssueResultResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("messageCode")
    val messageCode: Any,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("gameId")
        val gameId: Int,
        @SerializedName("issueNum")
        val issueNum: String,
        @SerializedName("winNum")
        val winNum: String
    )
}