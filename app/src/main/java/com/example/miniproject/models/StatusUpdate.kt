package com.example.miniproject.models


import com.google.gson.annotations.SerializedName

data class StatusUpdate(
    @SerializedName("status") val status: String
)