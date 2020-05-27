package com.griddynamics.connectedapps.gateway.network

import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import com.griddynamics.connectedapps.model.device.DeviceRequest
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.gateway.GatewayRequest
import com.griddynamics.connectedapps.model.metrics.MetricsRequest
import com.griddynamics.connectedapps.model.metrics.MetricsResponse

interface AirScannerRepository {
    suspend fun addDevice(request: DeviceRequest): Any
    suspend fun editDevice(request: DeviceResponse): Any
    suspend fun deleteDevice(request: DeviceRequest): Any
    suspend fun addGateway(request: GatewayRequest): Any
    suspend fun editGateway(request: GatewayRequest): Any
    suspend fun deleteGateway(request: GatewayRequest): Any
    suspend fun getMetrics(request: MetricsRequest): MetricsMap
}