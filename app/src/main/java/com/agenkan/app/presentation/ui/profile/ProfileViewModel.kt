package com.agenkan.app.presentation.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agenkan.app.data.ContentResult
import com.agenkan.app.data.model.Device
import com.agenkan.app.data.repository.DataRepository
import com.agenkan.app.presentation.util.PolicyManager
import com.agenkan.app.presentation.util.toSingleEvent
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val repo: DataRepository,
    private val policyManager: PolicyManager
) : ViewModel() {

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast.toSingleEvent()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    val device: LiveData<ContentResult<Device>> = repo.observeDevice()

    val appState = MutableLiveData<AppState>(
        AppState.AUTHENTICATED
    )

    fun logout() {
        viewModelScope.launch {
            policyManager.deactivate()
            repo.clearData()
            appState.value = AppState.UN_AUTHENTICATED
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            repo.getProfile()
        }
    }

    enum class AppState {
        UN_AUTHENTICATED, AUTHENTICATED, USER_CONNECTED
    }
}