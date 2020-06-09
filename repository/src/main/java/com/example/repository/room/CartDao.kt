package com.example.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {
    @Insert
    fun insertCart(cart: Cart)

    @Query("SELECT * FROM Cart")
    fun getCartList()
}