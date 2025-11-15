package com.example.miniproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "purchase_orders")
data class PurchaseOrder(

    @PrimaryKey
    @SerializedName("order_id")
    val id: Int,

    @SerializedName("item_name")
    val itemName: String,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("supplier")
    val supplier: String
)