package com.agenkan.app.presentation.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat


class DeviceDetailsProvider(val context: Context) {

    private val telephonyManager: TelephonyManager = context
        .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    val deviceId: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    val imei: String
        @SuppressLint("HardwareIds")
        get() {
            try {
                if (!checkPhoneStatePermission()) {
                    return ""
                }
                return telephonyManager.deviceId
            } catch (e: Exception) {
                return ""
            }
        }

    val deviceName: String
        @SuppressLint("DefaultLocale")
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
                capitalize(model)
            } else {
                capitalize(manufacturer) + " " + model
            }
        }

    private fun checkPhoneStatePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun capitalize(s: String?): String {
        if (s == null || s.isEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first) + s.substring(1)
        }
    }
}