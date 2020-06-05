package com.griddynamics.connectedapps.gateway.stream

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.model.device.DeviceResponse
import javax.inject.Singleton

@Singleton
class DeviceStreamImpl : DeviceStream {
    private val _scannerData by lazy { MutableLiveData(listOf<DeviceResponse>()) }
    override val scannerData: MutableLiveData<List<DeviceResponse>>
        get() = _scannerData
}