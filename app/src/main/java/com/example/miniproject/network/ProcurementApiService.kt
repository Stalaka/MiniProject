package com.example.miniproject.network

import com.example.miniproject.models.AuthResponse
import com.example.miniproject.models.LoginRequest
import com.example.miniproject.models.PassportMaterial
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProcurementApiService {

    // ✅ CORRECT: Login endpoint
    // Python urls.py must have: path('api/login/', ...)
    @POST("api/login/")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>

    // ⚠️ WARNING: Only call this if you created an 'api/materials/' endpoint in Django
    // If you haven't created a specific JSON view for materials yet, don't use this function.
    @GET("api/materials/")
    suspend fun getAllMaterials(): Response<List<PassportMaterial>>

    // ✅ CORRECT: Dashboard/Orders endpoint
    // Python urls.py must have: path('api/user/orders/', ...)
    // Python view must return a LIST: JsonResponse(data, safe=False)
    @GET("api/user/orders/")
    suspend fun getUserOrders(): Response<List<PassportMaterial>>

    // ✅ CORRECT: Procurement submission endpoint
    // Python urls.py must have: path('api/procure/', ...)
    @POST("api/procure/")
    suspend fun requestProcurement(@Body material: PassportMaterial): Response<Unit>
}