package com.griddynamics.connectedapps.gateway.api

import com.griddynamics.connectedapps.model.AddDeviceRequest
import com.griddynamics.connectedapps.model.GetDevicesResponse
import com.griddynamics.connectedapps.model.ScannersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AirScannerAPI {
    @GET("/api/devices")
    fun getScanners(): Response<GetDevicesResponse>

    @POST("/api/devices/add")
    fun addDevice(@Body device: AddDeviceRequest)
}