package com.agenkan.customer.di

import com.agenkan.customer.presentation.splash.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var viewModelModule = module {
    viewModel { MainViewModel(get()) }
}