package com.example.miniproject.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miniproject.R
import com.example.miniproject.adapters.MaterialAdapter
import com.example.miniproject.auth.TokenManager
import com.example.miniproject.database.AppDatabase
import com.example.miniproject.models.PassportMaterial
import com.example.miniproject.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PurchaseOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_orders)

        val recyclerView = findViewById<RecyclerView>(R.id.orders_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val tokenManager = TokenManager(this)
        val apiService = RetrofitClient.getAuthenticatedInstance(tokenManager)

        // 1. Initialize the Local Database
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            try {
                // --- ONLINE MODE ---
                // Try fetching from Python Server
                val response = apiService.getPurchaseOrders()

                if (response.isSuccessful && response.body() != null) {
                    val orders = response.body()!!

                    // 2. SAVE to Local Database (Cache)
                    withContext(Dispatchers.IO) {
                        db.orderDao().insertAll(orders)
                    }

                    // Convert to display model
                    val displayList = orders.map { order ->
                        PassportMaterial(
                            id = order.id,
                            name = order.itemName,
                            quantity = order.quantity,
                            status = "${order.status} • ${order.supplier}"
                        )
                    }

                    recyclerView.adapter = MaterialAdapter(displayList) { selectedItem ->
                        Toast.makeText(this@PurchaseOrderActivity, "Order: ${selectedItem.name}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@PurchaseOrderActivity, "Server Error", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                // OFFLINE MODE

                Toast.makeText(this@PurchaseOrderActivity, "Offline Mode: Loading local data", Toast.LENGTH_LONG).show()

                // 3. LOAD from Local Database if internet fails
                val offlineOrders = withContext(Dispatchers.IO) {
                    db.orderDao().getAllOrders()
                }

                if (offlineOrders.isNotEmpty()) {
                    val displayList = offlineOrders.map { order ->
                        PassportMaterial(
                            id = order.id,
                            name = order.itemName,
                            quantity = order.quantity,
                            status = "${order.status} • ${order.supplier} (Offline)"
                        )
                    }
                    recyclerView.adapter = MaterialAdapter(displayList) { }
                } else {
                    Toast.makeText(this@PurchaseOrderActivity, "No internet and no local data.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}