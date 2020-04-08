package com.agenkan.app.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "device_table")
data class Device(
    @PrimaryKey @ColumnInfo(name = "installId") var installId: Int,
    @ColumnInfo(name = "fcmPushToken") var fcmPushToken: String,
    @ColumnInfo(name = "deviceId") var deviceId: String?,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "imei") var imei: String?,
    @ColumnInfo(name = "agentId") var agentId: String?,
    @ColumnInfo(name = "blocked") var isBlocked: Boolean = false,
    @ColumnInfo(name = "connected") var isConnected: Boolean = false,
    @ColumnInfo(name = "latitude") var latitude: Double? = null,
    @ColumnInfo(name = "longitude") var longitude: Double? = null,
    @ColumnInfo(name = "altitude") var altitude: Double? = null,
    @ColumnInfo(name = "accuracy") var accuracy: Double? = null
) : Parcelable