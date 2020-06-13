package com.griddynamics.connectedapps.gateway.network

import com.griddynamics.connectedapps.gateway.network.api.AirScannerAPI
import com.griddynamics.connectedapps.gateway.network.api.GenericResponse
import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import com.griddynamics.connectedapps.model.device.DeviceRequest
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.model.gateway.GatewayRequest
import com.griddynamics.connectedapps.model.metrics.MetricsRequest
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG: String = "AirScannerGatewayImpl"

@Singleton
class AirScannerRepositoryImpl
@Inject constructor(private val api: AirScannerAPI) :
    AirScannerRepository {

    override suspend fun addDevice(request: DeviceResponse): GenericResponse<Any> {
        return api.addDevice(
            DeviceRequest(
                "${request.displayName}",
                "${request.deviceId}",
                "${request.dataFormat}",
                "${request.userId}",
                "${request.gatewayId}",
                "${request.location?.latitude}",
                "${request.location?.longitude}",
                "${request.locationDescription}",
                request.publicMetrics,
                request.metricsConfig
            )
        )
    }

    override suspend fun editDevice(request: DeviceResponse): GenericResponse<Any> {
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
                "${request.locationDescription}",
                request.publicMetrics,
                request.metricsConfig
            )
        )
    }

    override suspend fun deleteDevice(deviceId: String): GenericResponse<Any> {
        return api.deleteDevice(deviceId)
    }

    override suspend fun addGateway(request: GatewayResponse): GenericResponse<Any> {
        return api.addGateway(GatewayRequest(key = request.key, displayName = request.displayName))
    }

    override suspend fun editGateway(request: GatewayRequest): GenericResponse<Any> {
        return api.editGateway(request)
    }

    override suspend fun deleteGateway(gatewayId: String): GenericResponse<Any> {
        return api.deleteGateway(gatewayId)
    }

    override suspend fun getMetrics(request: MetricsRequest): GenericResponse<MetricsMap> {
        return api.getLastHourMetrics(request.id)
    }


}