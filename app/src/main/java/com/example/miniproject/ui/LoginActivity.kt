package com.example.miniproject.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.example.miniproject.R
import com.example.miniproject.auth.TokenManager
import com.example.miniproject.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize TokenManager immediately after setContentView
        tokenManager = TokenManager(applicationContext)

        // Check if already logged in
        if (tokenManager.isLoggedIn()) {
            startMainActivity()
            return
        }

        // Find Views
        val usernameEditText = findViewById<TextInputEditText>(R.id.edit_text_username)
        val passwordEditText = findViewById<TextInputEditText>(R.id.edit_text_password)
        val loginButton = findViewById<Button>(R.id.button_login)

        // Observe Login Success/Token
        viewModel.authToken.observe(this) { token ->
            if (token != null) {
                tokenManager.saveToken(token)
                Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()
                startMainActivity()
            }
        }

        // Observe Errors
        viewModel.errorMessage.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                loginButton.isEnabled = true
            }
        }

        // Handle Button Click
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginButton.isEnabled = false
                viewModel.login(username, password)
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}