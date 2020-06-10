package com.example.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.repository.model.base.ViewState

@Dao
interface CartDao {
    @Insert
    fun addCart(cart: Cart): Long

    @Query("SELECT * FROM Cart WHERE gameId LIKE :gameId")
    fun getCartList(gameId: Int): MutableList<Cart>

    @Query("SELECT DISTINCT gameId FROM Cart")
    fun getAllGameId(): MutableList<Int>

}