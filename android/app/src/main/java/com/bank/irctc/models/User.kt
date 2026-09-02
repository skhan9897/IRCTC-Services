package com.bank.irctc.models

import com.google.gson.annotations.SerializedName

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
    @SerializedName("userId")
    val id: Long,
    val name: String,
    val email: String,
    val role: String,
    val message: String? = null
)
