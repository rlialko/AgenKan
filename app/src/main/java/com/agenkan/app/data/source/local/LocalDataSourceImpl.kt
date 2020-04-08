package com.agenkan.app.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.agenkan.app.data.ContentResult
import com.agenkan.app.data.ContentResult.Error
import com.agenkan.app.data.model.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LocalDataSourceImpl(
    private val dataDao: DataDao
) : LocalDataSource {

    override suspend fun getDevice(id: Int): ContentResult<Device> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val device = dataDao.getDevice(id)
                device?.let {
                    return@withContext ContentResult.Success(it)
                }
                Error<Device>(Exception("no data"))
            } catch (e: Exception) {
                Error<Device>(e)
            }
        }

    override suspend fun saveDevice(device: Device) = withContext(Dispatchers.IO) {
        dataDao.insert(device)
    }

    override suspend fun deleteAllDevices() = withContext(Dispatchers.IO) {
        dataDao.deleteAll()
    }

    override fun observeDevice(id: Int): LiveData<ContentResult<Device>> {
        return Transformations.map(dataDao.observeDeviceById(id)) { transform(it) }
    }

    private fun transform(it: Device?): ContentResult<Device> {
        return if (it != null) ContentResult.Success(it)
        else Error(Exception("No data"))
    }

}