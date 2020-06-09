package com.example.repository.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cart (
    @PrimaryKey(autoGenerate = true)
    var cartId: Int,
    var betNumber: String,
    var gameTypeId: Int,
    var playTypeId: String
)