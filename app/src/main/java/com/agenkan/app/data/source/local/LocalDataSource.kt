package com.agenkan.app.data.source.local

import androidx.lifecycle.LiveData
import com.agenkan.app.data.ContentResult
import com.agenkan.app.data.model.Device

interface LocalDataSource {

    suspend fun getDevice(id: Int): ContentResult<Device>

    suspend fun saveDevice(device: Device)

    suspend fun deleteAllDevices()

    fun observeDevice(id: Int): LiveData<ContentResult<Device>>
}