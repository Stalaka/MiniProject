package com.example.miniproject.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)


data class AuthResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user_id")
    val userId: Int? = null
)