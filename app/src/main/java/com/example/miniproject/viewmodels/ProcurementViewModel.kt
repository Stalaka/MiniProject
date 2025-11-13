package com.example.miniproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.miniproject.models.PassportMaterial
import com.example.miniproject.network.ProcurementApiService
import kotlinx.coroutines.launch

class ProcurementViewModel(private val apiService: ProcurementApiService) : ViewModel() {


    private val _submissionStatus = MutableLiveData<Boolean?>()
    val submissionStatus: LiveData<Boolean?> = _submissionStatus


    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    fun submitRequest(name: String, quantity: Int, note: String) {

        _submissionStatus.value = null
        _error.value = null

        val requestBody = PassportMaterial(
            id = 0,
            name = name,
            quantity = quantity,
            status = "Pending: $note"
        )

        viewModelScope.launch {
            try {
                val response = apiService.requestProcurement(requestBody)

                if (response.isSuccessful) {
                    _submissionStatus.value = true // Success
                } else {
                    _error.value = "Submission failed: ${response.code()}"
                    _submissionStatus.value = false
                }
            } catch (e: Exception) {
                _error.value = "Network Error: Cannot connect to server."
                _submissionStatus.value = false
            }
        }
    }


    class Factory(private val apiService: ProcurementApiService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProcurementViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProcurementViewModel(apiService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}