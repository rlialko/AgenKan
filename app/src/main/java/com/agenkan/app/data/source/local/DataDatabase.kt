package com.agenkan.app.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agenkan.app.data.model.Device

@Database(entities = [Device::class], version = 1, exportSchema = false)
abstract class DataDatabase : RoomDatabase() {

    abstract fun getDao(): DataDao
}