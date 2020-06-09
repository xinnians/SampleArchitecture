package com.example.repository.room

class CartRepository(private val cartDao: CartDao) {

    fun insertCart(cart: Cart) = cartDao.insertCart(cart)
    fun getCartList() = cartDao.getCartList()

}