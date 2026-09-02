package com.bank.irctc.models

data class User(
    val id: Long? = null,
    val name: String,
    val email: String,
    val mobile: String,
    val password: String,
    val role: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val id: Long,
    val name: String,
    val email: String,
    val mobile: String,
    val role: String,
    val token: String? = null // Backend might return a token or just user details
)
