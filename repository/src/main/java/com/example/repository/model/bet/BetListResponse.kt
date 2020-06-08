package com.example.repository.model.bet

import com.example.repository.model.base.BaseResult
import com.google.gson.annotations.SerializedName

data class BetListResponse(
    @SerializedName("data")
    val data: Data
) : BaseResult() {
    data class Data(
        @SerializedName("betResultDetailEntityList")
        val betResultDetailEntityList: List<BetResultDetailEntity>,
        @SerializedName("failCount")
        val failCount: Int,
        @SerializedName("successCount")
        val successCount: Int,
        @SerializedName("balance")
        val balance: Int
    ) {
        data class BetResultDetailEntity(
            @SerializedName("id")
            val id: Int,
            @SerializedName("isSuccess")
            val isSuccess: Boolean,
            @SerializedName("message")
            val message: String,
            @SerializedName("messageCode")
            val messageCode: String,
            @SerializedName("uuid")
            val uuid: String
        )
    }
}