package com.example.repository.model

data class LoginParam (
    val account: String,
    val password: String,
    val merchantId: Int = 1
)