package com.agenkan.customer.di

import com.agenkan.customer.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module


val remoteDatabaseModule = module {
    factory { FirebaseAuth.getInstance() }
    factory { FirebaseFirestore.getInstance() }
    single { UserRepository(get(), get()) }
}
