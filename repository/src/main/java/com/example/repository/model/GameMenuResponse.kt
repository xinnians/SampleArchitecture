package com.example.repository.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GameMenuResponse(
    @SerializedName("data")
    val data: List<Data>
) : BaseResult() {
    data class Data(
        @SerializedName("gameInfoEntityList")
        val gameInfoEntityList: List<GameInfoEntity>,
        @SerializedName("gameTypeDisplayName")
        val gameTypeDisplayName: String,
        @SerializedName("id")
        val id: Int
    ) {
        @Parcelize
        data class GameInfoEntity(
            @SerializedName("gameId")
            val gameId: Int,
            @SerializedName("gameName")
            val gameName: String,
            @SerializedName("gameStatus")
            val gameStatus: Int,
            @SerializedName("gameTypeId")
            val gameTypeId: Int,
            @SerializedName("lockTime")
            val lockTime: Int,
            var isFavorite: Boolean = false
        ) : Parcelable
    }
}