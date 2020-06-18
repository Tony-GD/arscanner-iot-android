package com.griddynamics.connectedapps.repository.stream

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.model.device.DeviceResponse

interface DeviceStream {
    val publicDevices: MutableLiveData<List<DeviceResponse>>
    val userDevices: MediatorLiveData<List<DeviceResponse>>
}