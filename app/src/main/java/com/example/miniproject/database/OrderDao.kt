package com.example.miniproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
// 👇 THIS LINE IS CRITICAL. If it is missing, the build fails.
import com.example.miniproject.models.PurchaseOrder

@Dao
interface OrderDao {
    // 1. Get all orders from the local phone storage
    @Query("SELECT * FROM purchase_orders")
    fun getAllOrders(): List<PurchaseOrder>

    // 2. Save orders (Replace if ID exists)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<PurchaseOrder>)

    // 3. Delete everything
    @Query("DELETE FROM purchase_orders")
    suspend fun clearAll()
}