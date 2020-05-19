package com.example.repository.model

data class LoginRequest (
    val account: String,
    val password: String,
    val merchantId: Int = 1
)