package com.example.repository.model.bet

import com.example.repository.model.base.BaseResult
import com.google.gson.annotations.SerializedName

data class HistoricalResponse(
    @SerializedName("data")
    val data: List<Data>
) : BaseResult() {
    data class Data(
        @SerializedName("issueNum")
        val issueNum: String,
        @SerializedName("winNum")
        val winNum: String
    )
}