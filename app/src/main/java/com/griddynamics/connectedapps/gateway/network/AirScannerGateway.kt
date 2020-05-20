package com.griddynamics.connectedapps.gateway.network

import androidx.lifecycle.LiveData
import com.griddynamics.connectedapps.gateway.api.ApiResponse
import com.griddynamics.connectedapps.model.GetDevicesResponse

interface AirScannerGateway {
    fun getAirScanners(): LiveData<ApiResponse<GetDevicesResponse>>
}