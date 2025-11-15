package com.example.miniproject.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.miniproject.R
import com.example.miniproject.adapters.MaterialAdapter
import com.example.miniproject.auth.TokenManager
import com.example.miniproject.network.RetrofitClient
import com.example.miniproject.viewmodels.OrderViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: MaterialAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val viewModel: OrderViewModel by viewModels {
        val authenticatedApiService = RetrofitClient.getAuthenticatedInstance(tokenManager)
        OrderViewModel.Factory(authenticatedApiService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize TokenManager FIRST (Prevents the crash)
        tokenManager = TokenManager(applicationContext)

        // 2. Check Login
        if (!tokenManager.isLoggedIn()) {
            startLoginActivity()
            return
        }

        setContentView(R.layout.activity_main)

        // 3. Setup UI
        recyclerView = findViewById(R.id.materials_recycler_view)
        progressBar = findViewById(R.id.progress_bar)
        val fab = findViewById<FloatingActionButton>(R.id.fab_new_request)

        // ✅ UPDATED: Initialize Adapter with Click Listener for Detail View
        adapter = MaterialAdapter(emptyList()) { selectedItem ->
            // This code runs when you tap a card
            val intent = Intent(this, DetailActivity::class.java)
            // Pass data to the new screen
            intent.putExtra("ORDER_ID", selectedItem.id)
            intent.putExtra("ORDER_NAME", selectedItem.name)
            intent.putExtra("ORDER_STATUS", selectedItem.status)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 4. Observe Data
        viewModel.userOrders.observe(this) { orders ->
            adapter.updateData(orders)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                if (error.contains("Unauthorized")) {
                    startLoginActivity()
                }
            }
        }

        // FAB Click
        fab.setOnClickListener {
            startActivity(Intent(this, ProcurementActivity::class.java))
        }

        // Fetch Data
        viewModel.fetchUserOrders()
    }

    override fun onResume() {
        super.onResume()
        if (this::tokenManager.isInitialized && tokenManager.isLoggedIn()) {
            viewModel.fetchUserOrders()
        }
    }

    private fun startLoginActivity() {
        if (this::tokenManager.isInitialized) {
            tokenManager.clearToken()
        }
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // ✅ ADDED: Navigation to Purchase Order Activity
            R.id.action_purchase_orders -> {
                startActivity(Intent(this, PurchaseOrderActivity::class.java))
                true
            }
            R.id.action_logout -> {
                startLoginActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}