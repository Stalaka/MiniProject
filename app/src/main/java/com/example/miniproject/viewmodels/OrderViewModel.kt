package com.example.miniproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.miniproject.models.PassportMaterial
import com.example.miniproject.network.ProcurementApiService
import kotlinx.coroutines.launch

class OrderViewModel(private val apiService: ProcurementApiService) : ViewModel() {

    private val _userOrders = MutableLiveData<List<PassportMaterial>>()
    val userOrders: LiveData<List<PassportMaterial>> = _userOrders

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchUserOrders() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getUserOrders()

                if (response.isSuccessful) {
                    _userOrders.value = response.body() ?: emptyList()
                    _error.value = null
                } else if (response.code() == 401) {
                    _error.value = "Unauthorized. Please log in again."
                } else {
                    _error.value = "Error fetching orders: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network failure: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    class Factory(private val apiService: ProcurementApiService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OrderViewModel(apiService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}