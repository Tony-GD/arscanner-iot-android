package com.griddynamics.connectedapps.gateway.stream

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.gateway.api.ApiEmptyResponse
import com.griddynamics.connectedapps.gateway.api.ApiResponse
import com.griddynamics.connectedapps.model.GetDevicesResponse
import javax.inject.Singleton

@Singleton
class DeviceStreamImpl : DeviceStream {
    private val _scannerData by lazy { MutableLiveData(ApiEmptyResponse<GetDevicesResponse>()) }
    override val scannerData: MutableLiveData<ApiResponse<GetDevicesResponse>>
        get() = _scannerData as MutableLiveData<ApiResponse<GetDevicesResponse>>
}