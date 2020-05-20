package com.griddynamics.connectedapps.gateway.stream

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.gateway.api.ApiResponse
import com.griddynamics.connectedapps.model.GetDevicesResponse

interface DeviceStream {
    val scannerData: MutableLiveData<ApiResponse<GetDevicesResponse>>
}