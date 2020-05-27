package com.griddynamics.connectedapps.gateway.network

import com.griddynamics.connectedapps.gateway.network.api.AirScannerAPI
import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import com.griddynamics.connectedapps.model.device.DeviceRequest
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.gateway.GatewayRequest
import com.griddynamics.connectedapps.model.metrics.MetricsRequest
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG: String = "AirScannerGatewayImpl"

@Singleton
class AirScannerRepositoryImpl
@Inject constructor(private val api: AirScannerAPI) :
    AirScannerRepository {

    override suspend fun addDevice(request: DeviceRequest): Any = api.addDevice(request)
    override suspend fun editDevice(request: DeviceResponse): Any {
        return api.editDevice(
            "${request.deviceId}",
            DeviceRequest(
                "${request.displayName}",
                "${request.deviceId}",
                "${request.dataFormat}",
                "${request.userId}",
                "${request.gatewayId}",
                "${request.location?.latitude}",
                "${request.location?.longitude}",
                "${request.locationDescription}"
            )
        )
    }

    override suspend fun deleteDevice(request: DeviceRequest): Any {
        return api.deleteDevice(request)
    }

    override suspend fun addGateway(request: GatewayRequest): Any {
        return api.addGateway(request)
    }

    override suspend fun editGateway(request: GatewayRequest): Any {
        return api.editGateway(request)
    }

    override suspend fun deleteGateway(request: GatewayRequest): Any {
        return api.deleteGateway(request)
    }

    override suspend fun getMetrics(request: MetricsRequest): MetricsMap {
        return try {
            api.getLastHourMetrics(request.id)
        } catch (e: Exception) {
            emptyMap()
        }
    }


}