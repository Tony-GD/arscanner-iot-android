package com.griddynamics.connectedapps.gateway.api

import com.griddynamics.connectedapps.model.ScannersResponse
import retrofit2.Response

interface AirScannerAPI {
    fun getScanners(): Response<ScannersResponse>
}