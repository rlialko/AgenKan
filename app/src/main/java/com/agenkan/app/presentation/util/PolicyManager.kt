package com.agenkan.app.presentation.util

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Build


class PolicyManager(val context: Context) {

    private val devicePolicyManager: DevicePolicyManager = context
        .getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val devAdminReceiver: ComponentName?

    init {
        devAdminReceiver = ComponentName(
            context.packageName,
            context.packageName + ".presentation.receiver.MyDeviceAdminReceiver"
        )
    }

    val isAdminActive: Boolean
        get() = devicePolicyManager.isAdminActive(devAdminReceiver!!)

    fun getMac(): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                devicePolicyManager.getWifiMacAddress(devAdminReceiver!!).orEmpty()
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }


    fun lockNow() {
        devicePolicyManager.lockNow()
    }

    fun disableCamera() {
        if (!devicePolicyManager.getCameraDisabled(devAdminReceiver)) {
            devicePolicyManager.setCameraDisabled(devAdminReceiver!!, true)
        }
    }

    fun enableCamera() {
        if (devicePolicyManager.getCameraDisabled(devAdminReceiver)) {
            devicePolicyManager.setCameraDisabled(devAdminReceiver!!, false)
        }
    }

    fun deactivate() {
        devicePolicyManager.removeActiveAdmin(devAdminReceiver!!)
    }

}
