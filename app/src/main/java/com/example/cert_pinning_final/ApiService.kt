package com.example.cert_pinning_final

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginRequest>
}

data class LoginRequest(
    val username: String,
    val password: String
)
