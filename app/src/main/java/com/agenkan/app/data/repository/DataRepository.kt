package com.agenkan.app.data.repository

import androidx.lifecycle.LiveData
import com.agenkan.app.data.ContentResult
import com.agenkan.app.data.model.*


interface DataRepository {

    fun observeDevice(): LiveData<ContentResult<Device>>

    suspend fun registerDevice(registerRequest: RegisterRequest): ContentResult<RegisterResponse>

    suspend fun getProfile(): ContentResult<ProfileResponse>

    suspend fun updatePushToken(pushToken: String): ContentResult<DefaultResponse>

    suspend fun setBlocked(blocked: Boolean, passwordHash: String? = null)

    suspend fun clearData()

    fun isLoggedIn(): Boolean

    fun isBlocked(): Boolean

}
