package com.example.repository.model
import com.google.gson.annotations.SerializedName


data class PlayTypeInfoResponse(
    @SerializedName("data")
    val data: Data
) : BaseResult()

data class Data(
    @SerializedName("maxAmount")
    val maxAmount: Int,
    @SerializedName("minAmount")
    val minAmount: Int,
    @SerializedName("betTypeGroups")
    val betTypeGroupList: List<BetTypeGroups>
)

data class BetTypeGroups(
    @SerializedName("groupName")
    val grounpName: String,
    @SerializedName("betTypeEntityList")
    val betTypeEntityList: List<BetTypeEntity>
)

data class BetTypeEntity(
    @SerializedName("betTypeCode")
    val betTypeCode: Int,
    @SerializedName("betTypeDisplayName")
    val betTypeDisplayName: String,
    @SerializedName("betGroupEntityList")
    val mobileBetGroupEntityList: List<BetGroupEntity>,
    @SerializedName("status")
    val status: Int,
    var isSelect: Boolean = false
)

data class BetGroupEntity(
    @SerializedName("betGroupCode")
    val betGroupCode: Int,
    @SerializedName("betGroupDisplayName")
    val betGroupDisplayName: String,
    @SerializedName("playTypeInfoEntityList")
    val playTypeInfoEntityList: List<PlayTypeInfoEntity>,
    @SerializedName("status")
    val status: Int
)

data class PlayTypeInfoEntity(
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("exampleDescription")
    val exampleDescription: String,
    @SerializedName("isOpen")
    val isOpen: Boolean,
    @SerializedName("methodDescription")
    val methodDescription: String,
    @SerializedName("playTypeBonusEntityList")
    val playTypeBonusEntityList: List<PlayTypeBonusEntity>,
    @SerializedName("playTypeCode")
    val playTypeCode: Int,
    @SerializedName("regex")
    val regex: String,
    @SerializedName("status")
    val status: Int
)

data class PlayTypeBonusEntity(
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("playTypeBonus")
    val playTypeBonus: Float
)