package com.agenkan.app.data.source.remote

import com.agenkan.app.data.ContentResult
import com.agenkan.app.data.ContentResult.Error
import com.agenkan.app.data.ContentResult.Success
import com.agenkan.app.data.api.DataApiService
import com.agenkan.app.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RemoteDataSourceImpl(
    private val service: DataApiService
) : RemoteDataSource {

    override suspend fun updatePushToken(pushToken: PushTokenRequest): ContentResult<DefaultResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = service.updatePushToken(pushToken)
                if (response.isSuccessful && response.body() != null) {
                    return@withContext Success(response.body()!!)
                } else {
                    return@withContext Error<DefaultResponse>(Exception(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Error<DefaultResponse>(e)
            }
        }

    override suspend fun registerDevice(registerRequest: RegisterRequest): ContentResult<RegisterResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = service.registerDevice(registerRequest)
                if (response.isSuccessful && response.body() != null) {
                    return@withContext Success(response.body()!!)
                } else {
                    return@withContext Error<RegisterResponse>(Exception(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Error<RegisterResponse>(e)
            }
        }

    override suspend fun getProfile(): ContentResult<ProfileResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = service.getProfile()
                if (response.isSuccessful && response.body() != null) {
                    return@withContext Success(response.body()!!)
                } else {
                    return@withContext Error<ProfileResponse>(Exception(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Error<ProfileResponse>(e)
            }
        }

    override suspend fun sendVerificationInfo(verificationInfoRequest: VerificationInfoRequest): ContentResult<DefaultResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = service.sendVerificationInfo(verificationInfoRequest)
                if (response.isSuccessful && response.body() != null) {
                    return@withContext Success(response.body()!!)
                } else {
                    return@withContext Error<DefaultResponse>(Exception(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Error<DefaultResponse>(e)
            }
        }
}