package com.agenkan.app.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.agenkan.app.data.preferences.AppPreferences
import com.agenkan.app.presentation.service.ProtectService
import com.agenkan.app.presentation.ui.MainActivity
import org.koin.core.KoinComponent
import org.koin.core.inject


class BootReceiver : BroadcastReceiver(), KoinComponent {

    private val pref: AppPreferences by inject()

    override fun onReceive(context: Context, intent: Intent) {


        if (pref.getIsBlocked()!!) {
            Log.i("AGEN", "BootReceiver. onReceive. IsBlocked = ${pref.getIsBlocked()!!}")
            context.startActivity(MainActivity.getRestartIntent(context))
            ProtectService.start(context)
        }
    }

}