package com.agenkan.app.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agenkan.app.data.model.Device

@Dao
interface DataDao {

    @Query("SELECT * from device_table ORDER BY installId ASC")
    suspend fun getDevices(): List<Device>

    @Query("SELECT * FROM device_table WHERE installId=:id")
    fun observeDeviceById(id: Int): LiveData<Device>

    @Query("SELECT * from device_table WHERE installId=:id")
    suspend fun getDevice(id: Int): Device?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: Device)

    @Query("DELETE FROM device_table")
    suspend fun deleteAll()
}