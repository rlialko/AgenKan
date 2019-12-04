package com.agenkan.customer.di

import com.agenkan.customer.data.preferences.AgenKanPreferences
import com.agenkan.customer.data.repository.UserRepository
import com.agenkan.customer.presentation.main.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


var appModule = module {
    single { AgenKanPreferences(get()) }
}

val remoteDatabaseModule = module {
    factory { FirebaseAuth.getInstance() }
    factory { FirebaseFirestore.getInstance() }
}

val repositoryModule = module {
    single { UserRepository(get(), get(), get()) }
}

var viewModelModule = module {
    viewModel { MainViewModel(get()) }
}
