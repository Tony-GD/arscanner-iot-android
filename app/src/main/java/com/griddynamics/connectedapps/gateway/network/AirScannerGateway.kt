package com.griddynamics.connectedapps.gateway.network

import androidx.lifecycle.LiveData
import com.griddynamics.connectedapps.model.GetDevicesResponse

interface AirScannerGateway {
    fun getAirScanners(): LiveData<GetDevicesResponse>
}