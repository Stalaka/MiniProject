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

    // 2. Dashboard (User Orders) Endpoint
    @GET("api/user/orders/")
    suspend fun getUserOrders(): Response<List<PassportMaterial>>

    // 3. Procurement Submission Endpoint
    @POST("api/procure/")
    suspend fun requestProcurement(@Body material: PassportMaterial): Response<Unit>

    // 4. ✅ NEW: Fetch Purchase Orders List
    // Matches Django: path('api/purchase-orders/', ...)
    @GET("api/purchase-orders/")
    suspend fun getPurchaseOrders(): Response<List<PurchaseOrder>>

    // 5. ✅ NEW: Update Order Status Endpoint
    // Matches Django: path('api/orders/<int:order_id>/update/', ...)
    @POST("api/orders/{id}/update/")
    suspend fun updateOrderStatus(
        @Path("id") id: Int,
        @Body status: StatusUpdate
    ): Response<Unit>

    // (Optional Legacy Endpoint - keep if you use it, remove if not)
    @GET("api/materials/")
    suspend fun getAllMaterials(): Response<List<PassportMaterial>>
}