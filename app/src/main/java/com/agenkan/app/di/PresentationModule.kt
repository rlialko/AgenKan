package com.agenkan.app.di

import com.agenkan.app.presentation.ui.profile.ProfileViewModel
import com.agenkan.app.presentation.ui.registration.RegistrationViewModel
import com.agenkan.app.presentation.util.DeviceDetailsProvider
import com.agenkan.app.presentation.util.PolicyManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


var presentationModule = module {
    viewModel { RegistrationViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    single { PolicyManager(get()) }
    single { DeviceDetailsProvider(get()) }
}
