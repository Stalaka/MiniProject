package com.example.miniproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.miniproject.models.LoginRequest
import com.example.miniproject.network.RetrofitClient

class LoginViewModel : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _authToken = MutableLiveData<String?>()
    val authToken: LiveData<String?> = _authToken

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun login(username: String, password: String) {

        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val request = LoginRequest(username, password)
                // Uses the unauthenticated API instance
                val response = RetrofitClient.unauthenticatedInstance.loginUser(request)

                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse?.token?.isNotEmpty() == true) {
                        _authToken.value = authResponse.token
                        _loginSuccess.value = true
                    } else {
                        _errorMessage.value = "Login failed: Token not received."
                        _loginSuccess.value = false
                    }
                } else {
                    _errorMessage.value = "Login failed: Invalid credentials or server error (${response.code()})."
                    _loginSuccess.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network Error: Could not connect to server."
                _loginSuccess.value = false
            }
        }
    }
}