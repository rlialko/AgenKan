package com.agenkan.app.presentation.ui.registration

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agenkan.app.data.ContentResult
import com.agenkan.app.data.model.RegisterRequest
import com.agenkan.app.data.repository.DataRepository
import com.agenkan.app.presentation.util.DeviceDetailsProvider
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.launch


class RegistrationViewModel(
    private val repo: DataRepository,
    private val deviceDetailsProvider: DeviceDetailsProvider,
    private val firebaseInstanceId: FirebaseInstanceId
) : ViewModel() {

    private var fcmToken: String? = null
    val authenticationState = MutableLiveData<AuthenticationState>(
        AuthenticationState.UN_AUTHENTICATED
    )

    fun init() {
        if (repo.isLoggedIn()) {
            if (repo.isBlocked()) {
                authenticationState.value = AuthenticationState.AUTHENTICATED_BLOCKED
            } else {
                authenticationState.value = AuthenticationState.AUTHENTICATED
            }
        }
        firebaseInstanceId.instanceId.addOnSuccessListener {
            fcmToken = it.token
        }
    }

    fun register(location: Location) {
        viewModelScope.launch {
            authenticationState.value = AuthenticationState.PENDING_AUTHENTICATION
            val request = RegisterRequest(
                deviceDetailsProvider.deviceId,
                deviceDetailsProvider.deviceName,
                deviceDetailsProvider.imei,
                fcmToken ?: firebaseInstanceId.token.orEmpty(),
                listOf(
                    location.longitude,
                    location.latitude,
                    location.altitude,
                    location.accuracy.toDouble()
                )
            )
            val registerResult = repo.registerDevice(request)

            if (registerResult is ContentResult.Success) {
                authenticationState.value = AuthenticationState.AUTHENTICATED
            } else if (registerResult is Error) {
                authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
            }
        }
    }

    enum class AuthenticationState {
        UN_AUTHENTICATED, PENDING_AUTHENTICATION, INVALID_AUTHENTICATION, AUTHENTICATED, AUTHENTICATED_BLOCKED
    }
}