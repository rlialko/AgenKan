package com.agenkan.app

import android.app.Application
import com.agenkan.app.di.dataModule
import com.agenkan.app.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AgenKanApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AgenKanApp)
            modules(listOf(dataModule, presentationModule))
        }
    }
}