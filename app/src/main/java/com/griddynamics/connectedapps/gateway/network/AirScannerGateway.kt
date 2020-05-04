package com.griddynamics.connectedapps.gateway.network

import androidx.lifecycle.LiveData
import com.griddynamics.connectedapps.model.ScannersResponse

interface AirScannerGateway {
    fun getAirScanners(): LiveData<ScannersResponse>
    fun getName(): LiveData<String>
    fun getName2()
}