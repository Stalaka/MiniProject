package com.example.miniproject.models

import com.google.gson.annotations.SerializedName

data class PassportMaterial(
    // Example fields - adjust names/types based on your API
    @SerializedName("material_id")
    val id: Int,

    @SerializedName("material_name")
    val name: String,

    @SerializedName("current_stock")
    val quantity: Int,

    @SerializedName("procurement_status")
    val status: String
)