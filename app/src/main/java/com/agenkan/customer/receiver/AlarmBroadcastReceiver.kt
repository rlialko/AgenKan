package com.agenkan.customer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.agenkan.customer.data.preferences.AgenKanPreferences
import com.agenkan.customer.presentation.main.MainActivity
import org.koin.core.KoinComponent
import org.koin.core.inject


class AlarmBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val pref: AgenKanPreferences by inject()

    override fun onReceive(context: Context, intent: Intent) {

        Toast.makeText(
            context,
            "Alarm Manager just ran",
            Toast.LENGTH_SHORT
        ).show()

        if (pref.getIsBlocked()!!) {
            context.startActivity(MainActivity.getIntent(context))
        }
    }

}