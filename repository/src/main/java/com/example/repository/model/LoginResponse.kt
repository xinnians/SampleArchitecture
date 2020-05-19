package com.example.repository.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("messageCode")
    val messageCode: Any,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("expire")
        val expire: Int,
        @SerializedName("expireAt")
        val expireAt: Long,
        @SerializedName("refreshToken")
        val refreshToken: String,
        @SerializedName("token")
        val token: String
    )
}