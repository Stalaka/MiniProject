package com.example.miniproject.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.miniproject.R
import com.example.miniproject.auth.TokenManager
import com.example.miniproject.models.StatusUpdate
import com.example.miniproject.network.RetrofitClient
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 1. Get Data from Intent
        val name = intent.getStringExtra("ORDER_NAME") ?: "Unknown"
        val currentStatus = intent.getStringExtra("ORDER_STATUS") ?: "Unknown"
        val orderId = intent.getIntExtra("ORDER_ID", -1)

        // 2. Setup Views
        val nameText = findViewById<TextView>(R.id.detail_name)
        val statusText = findViewById<TextView>(R.id.detail_status)
        val updateButton = findViewById<Button>(R.id.btn_update_status)

        nameText.text = name
        statusText.text = currentStatus

        // 3. Setup API Service
        val tokenManager = TokenManager(this)
        val apiService = RetrofitClient.getAuthenticatedInstance(tokenManager)

        // 4. Handle Button Click
        updateButton.setOnClickListener {
            if (orderId == -1) {
                Toast.makeText(this, "Error: Invalid Order ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            updateButton.isEnabled = false // Disable button while loading
            updateButton.text = "Updating..."

            // Launch network call in background
            lifecycleScope.launch {
                try {
                    val newStatus = "In Production" // The status we want to set
                    val requestBody = StatusUpdate(newStatus)

                    val response = apiService.updateOrderStatus(orderId, requestBody)

                    if (response.isSuccessful) {
                        Toast.makeText(this@DetailActivity, "Status Updated!", Toast.LENGTH_SHORT).show()
                        statusText.text = newStatus // Update UI immediately
                        updateButton.text = "Update Complete"
                    } else {
                        Toast.makeText(this@DetailActivity, "Failed: ${response.code()}", Toast.LENGTH_LONG).show()
                        updateButton.isEnabled = true
                        updateButton.text = "Mark as In Production"
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, "Network Error", Toast.LENGTH_SHORT).show()
                    updateButton.isEnabled = true
                    updateButton.text = "Mark as In Production"
                }
            }
        }
    }
}