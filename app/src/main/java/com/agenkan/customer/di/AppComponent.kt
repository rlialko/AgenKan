package com.agenkan.customer.di

import org.koin.core.module.Module

val appComponent: List<Module> = listOf(remoteDatabaseModule, viewModelModule)
