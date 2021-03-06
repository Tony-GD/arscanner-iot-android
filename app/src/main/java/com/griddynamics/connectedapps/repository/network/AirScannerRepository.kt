package com.griddynamics.connectedapps.repository.network

import com.griddynamics.connectedapps.repository.network.api.GenericResponse
import com.griddynamics.connectedapps.repository.network.api.MetricsMap
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.model.metrics.MetricsRequest

interface AirScannerRepository {
    suspend fun addDevice(request: DeviceResponse): GenericResponse<Any>
    suspend fun editDevice(request: DeviceResponse): GenericResponse<Any>
    suspend fun deleteDevice(deviceId: String): GenericResponse<Any>
    suspend fun addGateway(request: GatewayResponse): GenericResponse<Any>
    suspend fun editGateway(request: GatewayResponse): GenericResponse<Any>
    suspend fun deleteGateway(gatewayId: String): GenericResponse<Any>
    suspend fun getMetrics(request: MetricsRequest): GenericResponse<MetricsMap>
}