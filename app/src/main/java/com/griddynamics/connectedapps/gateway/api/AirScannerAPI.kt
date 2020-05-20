package com.griddynamics.connectedapps.gateway.api

import androidx.lifecycle.LiveData
import com.griddynamics.connectedapps.model.DeviceRequest
import com.griddynamics.connectedapps.model.GetDevicesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AirScannerAPI {
    @GET("/api/devices")
    fun getScanners(): LiveData<ApiResponse<GetDevicesResponse>>

    @POST("/api/devices/add")
    fun addDevice(@Body device: DeviceRequest): LiveData<ApiResponse<Any>>

    @POST("/api/devices/remove")
    fun removeDevice(@Body device: DeviceRequest): LiveData<ApiResponse<Any>>
}