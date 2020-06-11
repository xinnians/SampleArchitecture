package com.example.repository.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cart (
    @PrimaryKey(autoGenerate = true)
    var cartId: Int, //db id
    var issueId: Int,//期號
    var gameId: Int,
    var playTypeCode: Int,//玩法
    var betNumber: String,//投注號碼
    var betCurrency: Int,//元角分厘
    var betUnit: Double,//單位
    var multiple: Int,//倍數
    var rebate: Double,//返點
    var uuid: String,
    var betCount: Int,//注數
    var amount: Int,//金額
    var isAppend: Boolean = false, //追單
    var isWinStop: Boolean = false, //追中即停
    var appendCount: Int = 0 //追單期數
)