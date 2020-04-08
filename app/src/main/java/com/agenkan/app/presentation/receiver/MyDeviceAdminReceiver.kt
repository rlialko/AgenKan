package com.agenkan.app.presentation.receiver

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent

class MyDeviceAdminReceiver : DeviceAdminReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

    }

    override fun onEnabled(context: Context?, intent: Intent?) {
        super.onEnabled(context, intent)
    }
}