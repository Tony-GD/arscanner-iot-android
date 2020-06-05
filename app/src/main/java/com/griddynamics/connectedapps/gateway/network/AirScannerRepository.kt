package com.griddynamics.connectedapps.gateway.network

import com.griddynamics.connectedapps.gateway.network.api.GenericResponse
import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.model.gateway.GatewayRequest
import com.griddynamics.connectedapps.model.metrics.MetricsRequest

interface AirScannerRepository {
    suspend fun addDevice(request: DeviceResponse): GenericResponse<Any>
    suspend fun editDevice(request: DeviceResponse): GenericResponse<Any>
    suspend fun deleteDevice(request: DeviceResponse): Any
    suspend fun addGateway(request: GatewayResponse): GenericResponse<Any>
    suspend fun editGateway(request: GatewayRequest): Any
    suspend fun deleteGateway(request: GatewayRequest): Any
    suspend fun getMetrics(request: MetricsRequest): MetricsMap
}