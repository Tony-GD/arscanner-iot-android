package com.griddynamics.connectedapps.gateway.network.api

import com.griddynamics.connectedapps.model.device.DeviceRequest
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.gateway.GatewayRequest
import retrofit2.http.*

const val addDevice = "api/device"
const val deleteDevice = "api/device"
const val editDevice = "api/device"
const val addGateway = "api/gateway"
const val deleteGateway = "api/gateway"
const val editGateway = "api/gateway"

const val metrics = "metrics/v1/device"
const val lastHour = "lastHour"

typealias MetricsMap = Map<String, List<Map<String, String>>>
typealias MutableMetricsMap = MutableMap<String, MutableList<MutableMap<String, String>>>
typealias GenericResponse<S> = NetworkResponse<S, Any>
interface AirScannerAPI {

    @POST(addDevice)
    suspend fun addDevice(@Body device: DeviceRequest): GenericResponse<Any>

    @DELETE("$deleteDevice/{id}")
    suspend fun deleteDevice(@Body device: DeviceResponse): Any

    @PUT("$editDevice/{id}")
    suspend fun editDevice(@Path("id") id: String, @Body device: DeviceRequest): GenericResponse<Any>

    @POST(addGateway)
    suspend fun addGateway(@Body device: GatewayRequest): GenericResponse<Any>

    @DELETE(deleteGateway)
    suspend fun deleteGateway(@Body device: GatewayRequest): Any

    @PUT(editGateway)
    suspend fun editGateway(@Body device: GatewayRequest): Any

    @GET("""$metrics/{id}/$lastHour""")
    suspend fun getLastHourMetrics(
        @Path("id") id: String
    ): MetricsMap
}