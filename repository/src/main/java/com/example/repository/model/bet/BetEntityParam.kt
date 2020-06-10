package com.example.repository.model.bet

import com.google.gson.annotations.SerializedName


data class BetEntityParam(
    @SerializedName("issueId")
    val issueId: Int,
    @SerializedName("bonusOrder")
    val bonusOrder: ArrayList<BonusOrderEntity>
)

data class BonusOrderEntity(
    @SerializedName("betCurrency")
    val betCurrency: Int,
    @SerializedName("betUnit")
    val betUnit: Double,
    @SerializedName("multiple")
    val multiple: Int,
    @SerializedName("rebate")
    val rebate: Double,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("playTypeCode")
    val playTypeCode: Int,
    @SerializedName("betNumber")
    val betNumber: String,
    @SerializedName("betCount")
    val betCount: Int
)