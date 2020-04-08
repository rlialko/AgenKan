package com.agenkan.app.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeviceResponse(
    var installId: Int,
    var fcmPushToken: String,
    var deviceId: String?,
    var name: String?,
    var imei: String?,
    var agentId: String?,
    var blocked: Boolean = false,
    var connected: Boolean = false,
    var location: List<Double>
) : Parcelable

fun DeviceResponse.toDevice(): Device {
    return Device(
        installId,
        fcmPushToken,
        deviceId,
        name,
        imei,
        agentId,
        blocked,
        connected,
        longitude = location.getOrNull(0),
        latitude = location.getOrNull(1),
        altitude = location.getOrNull(2),
        accuracy = location.getOrNull(3)
    )
}