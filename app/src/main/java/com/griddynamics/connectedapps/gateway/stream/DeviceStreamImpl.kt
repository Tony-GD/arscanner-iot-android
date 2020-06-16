package com.griddynamics.connectedapps.gateway.stream

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.model.device.DeviceResponse
import javax.inject.Singleton

@Singleton
class DeviceStreamImpl : DeviceStream {
    private val _publicDevices by lazy { MutableLiveData(listOf<DeviceResponse>()) }
    override val publicDevices: MutableLiveData<List<DeviceResponse>>
        get() = _publicDevices

    private val _userDevices = MediatorLiveData<List<DeviceResponse>>()
    override val userDevices: MediatorLiveData<List<DeviceResponse>>
        get() = _userDevices

}