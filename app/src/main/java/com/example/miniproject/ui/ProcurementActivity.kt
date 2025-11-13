package com.example.miniproject.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.miniproject.R
import com.example.miniproject.auth.TokenManager
import com.example.miniproject.network.RetrofitClient
import com.example.miniproject.viewmodels.ProcurementViewModel
import com.google.android.material.textfield.TextInputEditText

class ProcurementActivity : AppCompatActivity() {

    // 1. Declare TokenManager
    private lateinit var tokenManager: TokenManager


    private val viewModel: ProcurementViewModel by viewModels {


        tokenManager = TokenManager(applicationContext)

        ProcurementViewModel.Factory(
            RetrofitClient.getAuthenticatedInstance(tokenManager)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_procurement)


        val nameEditText = findViewById<TextInputEditText>(R.id.edit_text_material_name)
        val quantityEditText = findViewById<TextInputEditText>(R.id.edit_text_quantity)
        val noteEditText = findViewById<TextInputEditText>(R.id.edit_text_note)
        val submitButton = findViewById<Button>(R.id.button_submit_request)


        viewModel.submissionStatus.observe(this) { isSuccess ->
            submitButton.isEnabled = true
            if (isSuccess == true) {
                Toast.makeText(this, "Procurement request submitted!", Toast.LENGTH_LONG).show()
                finish()
            }
        }


        viewModel.error.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }


        submitButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val quantityString = quantityEditText.text.toString().trim()
            val note = noteEditText.text.toString().trim()

            if (name.isEmpty() || quantityString.isEmpty()) {
                Toast.makeText(this, "Material Name and Quantity are required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val quantity = quantityString.toInt()
                submitButton.isEnabled = false

                viewModel.submitRequest(name, quantity, note)
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Quantity must be a valid number.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}