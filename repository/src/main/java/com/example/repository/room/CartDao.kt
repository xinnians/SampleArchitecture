package com.example.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CartDao {
    @Insert
    fun insertCart(cart: Cart): Long

    @Query("SELECT * FROM Cart")
    fun getCartList(): List<Cart>

}