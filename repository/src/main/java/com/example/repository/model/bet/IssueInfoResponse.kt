package com.example.repository.model.bet

import com.example.repository.model.base.BaseResult
import com.google.gson.annotations.SerializedName

data class IssueInfoResponse(
    @SerializedName("data")
    val data: Data?
): BaseResult() {
    data class Data(
        @SerializedName("buyEndTime")
        val buyEndTime: Long,
        @SerializedName("endTime")
        val endTime: Long,
        @SerializedName("issueId")
        val issueId: Int,
        @SerializedName("issueNum")
        val issueNum: String,
        @SerializedName("nowTime")
        val nowTime: Long
    )
}