package com.agenkan.app.data.repository

import androidx.lifecycle.LiveData
import com.agenkan.app.data.ContentResult
import com.agenkan.app.data.model.*
import com.agenkan.app.data.preferences.AppPreferences
import com.agenkan.app.data.source.local.LocalDataSource
import com.agenkan.app.data.source.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DataRepositoryImpl(
    private val preferences: AppPreferences,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : DataRepository {

    override suspend fun registerDevice(registerRequest: RegisterRequest): ContentResult<RegisterResponse> =
        withContext(Dispatchers.IO) {
            return@withContext registerRemote(registerRequest)
        }

    override suspend fun getProfile(): ContentResult<ProfileResponse> =
        withContext(Dispatchers.IO) {
            return@withContext getProfileRemote()
        }

    override suspend fun updatePushToken(pushToken: String): ContentResult<DefaultResponse> =
        withContext(Dispatchers.IO) {
            return@withContext remoteDataSource.updatePushToken(PushTokenRequest(pushToken))
        }

    override fun observeDevice(): LiveData<ContentResult<Device>> {
        return localDataSource.observeDevice(preferences.getId())
    }

    override suspend fun setBlocked(blocked: Boolean, passwordHash: String?) {
        withContext(Dispatchers.IO) {
            val deviceResult = localDataSource.getDevice(preferences.getId())
            if (deviceResult is ContentResult.Success) {
                val device = deviceResult.data
                device.isBlocked = blocked
                localDataSource.saveDevice(device = device)
                preferences.putIsBlocked(blocked)
                preferences.putHash(passwordHash)
            }
        }
    }

    override suspend fun clearData() {
        withContext(Dispatchers.IO) {
            localDataSource.deleteAllDevices()
            preferences.clear()
        }
    }

    private suspend fun registerRemote(registerRequest: RegisterRequest): ContentResult<RegisterResponse> {
        return when (val registerResponse = remoteDataSource.registerDevice(registerRequest)) {
            is ContentResult.Success -> {
                refreshLocalDataSource(registerResponse.data)
                registerResponse
            }
            else -> registerResponse
        }
    }

    private suspend fun getProfileRemote(): ContentResult<ProfileResponse> {
        return when (val profileResponse = remoteDataSource.getProfile()) {
            is ContentResult.Success -> {
                localDataSource.saveDevice(profileResponse.data.deviceData.toDevice())
                profileResponse
            }
            else -> profileResponse
        }
    }

    private suspend fun refreshLocalDataSource(response: RegisterResponse) {
        localDataSource.saveDevice(response.deviceData.toDevice())
        preferences.putToken(response.token)
        preferences.putId(response.deviceData.installId)
    }

    override fun isLoggedIn(): Boolean {
        return preferences.getToken().isNotEmpty()
    }

    override fun isBlocked(): Boolean {
        return preferences.getIsBlocked()!!
    }

}