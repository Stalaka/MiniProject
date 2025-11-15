package com.example.miniproject.network

import com.example.miniproject.models.AuthResponse
import com.example.miniproject.models.LoginRequest
import com.example.miniproject.models.PassportMaterial
import com.example.miniproject.models.PurchaseOrder // Ensure you created this model
import com.example.miniproject.models.StatusUpdate  // Ensure you created this model
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProcurementApiService {

    // 1. Login Endpoint
    @POST("api/login/")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>

    // 2. Dashboard Endpoint
    @GET("api/user/orders/")
    suspend fun getUserOrders(): Response<List<PassportMaterial>>

    // 3. Procurement Submission Endpoint
    @POST("api/procure/")
    suspend fun requestProcurement(@Body material: PassportMaterial): Response<Unit>

    // 4.Fetch Purchase Orders List
    @GET("api/purchase-orders/")
    suspend fun getPurchaseOrders(): Response<List<PurchaseOrder>>

    // 5.Update Order Status Endpoint
    @POST("api/orders/{id}/update/")
    suspend fun updateOrderStatus(
        @Path("id") id: Int,
        @Body status: StatusUpdate
    ): Response<Unit>


    @GET("api/materials/")
    suspend fun getAllMaterials(): Response<List<PassportMaterial>>
}