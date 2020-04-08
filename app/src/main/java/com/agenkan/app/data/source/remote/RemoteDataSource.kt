package com.agenkan.app.data.source.remote

import com.agenkan.app.data.ContentResult
import com.agenkan.app.data.model.*


interface RemoteDataSource {

    suspend fun updatePushToken(pushToken: PushTokenRequest): ContentResult<DefaultResponse>

    suspend fun registerDevice(registerRequest: RegisterRequest): ContentResult<RegisterResponse>

    suspend fun getProfile(): ContentResult<ProfileResponse>

    suspend fun sendVerificationInfo(verificationInfoRequest: VerificationInfoRequest): ContentResult<DefaultResponse>
}