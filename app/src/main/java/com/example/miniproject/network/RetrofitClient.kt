package com.example.miniproject.network

import com.example.miniproject.auth.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    val unauthenticatedInstance: ProcurementApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProcurementApiService::class.java)
    }

    fun getAuthenticatedInstance(tokenManager: TokenManager): ProcurementApiService {

        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val token = tokenManager.getToken()

            val newRequest = if (token != null) {
                originalRequest.newBuilder()
                    .header("Authorization", "Token $token")
                    .build()
            } else {
                originalRequest
            }
            chain.proceed(newRequest)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProcurementApiService::class.java)
    }
}