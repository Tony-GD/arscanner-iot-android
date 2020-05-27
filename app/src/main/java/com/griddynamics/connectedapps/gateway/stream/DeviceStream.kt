package com.griddynamics.connectedapps.gateway.stream

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.model.device.DeviceResponse

interface DeviceStream {
    val scannerData: MutableLiveData<List<DeviceResponse>>
}