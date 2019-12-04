package com.agenkan.customer.application

import android.app.Application
import com.agenkan.customer.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AgenKanApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AgenKanApp)
            modules(appComponent)
        }

    }
}