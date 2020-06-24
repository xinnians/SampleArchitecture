package com.example.repository.room

import androidx.room.*

@Dao
interface CartDao {
    @Insert
    fun addCart(cart: Cart): Long

    @Query("SELECT * FROM Cart")
    fun getAllCartList(): MutableList<Cart>

    @Query("SELECT * FROM Cart WHERE gameId LIKE :gameId")
    fun getCartList(gameId: Int): MutableList<Cart>

    @Query("DELETE FROM Cart WHERE gameId LIKE :gameId")
    fun delCartById(gameId: Int): Int

    @Query("SELECT DISTINCT gameId FROM Cart")
    fun getAllGameId(): MutableList<Int>

    @Delete
    fun delCart(cart: Cart): Int

    @Update
    fun updateCart(cart: Cart): Int

}