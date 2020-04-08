package com.agenkan.app.data.api

import com.agenkan.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface DataApiService {

    @POST("devices/register")
    @Headers("No-Authentication: true")
    suspend fun registerDevice(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("devices/me")
    suspend fun getProfile(): Response<ProfileResponse>

    @POST("devices/verificationInfo")
    suspend fun sendVerificationInfo(@Body verificationInfoRequest: VerificationInfoRequest): Response<DefaultResponse>

    @POST("devices/locations")
    suspend fun sendLocation(@Body verificationInfoRequest: VerificationInfoRequest): Response<DefaultResponse>

    @PUT("devices/me")
    suspend fun updatePushToken(@Body pushToken: PushTokenRequest): Response<DefaultResponse>
}