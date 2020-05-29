package com.griddynamics.connectedapps.gateway.network

import android.util.Log
import com.griddynamics.connectedapps.gateway.network.api.AirScannerAPI
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

    override suspend fun addDevice(request: DeviceResponse): Any {
        return api.addDevice(
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

    override suspend fun deleteDevice(request: DeviceResponse): Any {
        return api.deleteDevice(request)
    }

    override suspend fun addGateway(request: GatewayResponse): Any {
        return try{
            api.addGateway(GatewayRequest(key = request.key, displayName = request.displayName))
        } catch (e: Exception) {
            Log.e(TAG, "AirScannerRepositoryImpl: addGateway", e)
            Any()
        }
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
            Log.e(TAG, "AirScannerRepositoryImpl: getMetrics", e)
            object : MetricsMap {
                private val origin = HashMap<String, List<Map<String, String>>>()
                override val entries: Set<Map.Entry<String, List<Map<String, String>>>>
                    get() = origin.entries
                override val keys: Set<String>
                    get() = origin.keys
                override val size: Int
                    get() = origin.size
                override val values: Collection<List<Map<String, String>>>
                    get() = origin.values

                override fun containsKey(key: String): Boolean = origin.containsKey(key)

                override fun containsValue(value: List<Map<String, String>>): Boolean =
                    origin.containsValue(value)

                override fun get(key: String): List<Map<String, String>>? = origin[key]

                override fun isEmpty(): Boolean = origin.isEmpty()

            }
        }
    }


}