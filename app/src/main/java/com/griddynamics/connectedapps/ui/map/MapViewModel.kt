package com.griddynamics.connectedapps.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.gateway.network.AirScannerRepository
import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import com.griddynamics.connectedapps.gateway.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.gateway.stream.DeviceStream
import com.griddynamics.connectedapps.gateway.stream.GatewayStream
import com.griddynamics.connectedapps.gateway.stream.MetricsStream
import com.griddynamics.connectedapps.model.metrics.MetricsRequest
import com.griddynamics.connectedapps.model.metrics.MetricsResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val deviceStream: DeviceStream,
    private val gatewayStream: GatewayStream,
    private val metricsStream: MetricsStream,
    private val repository: AirScannerRepository
) : ViewModel() {
    val devices = deviceStream.scannerData
    val gateways = gatewayStream.gatewayData
    val metrics = MutableLiveData<MetricsResponse>()

    fun loadDevices() {
        FirebaseAPI.getPublicDevices { devices ->
            deviceStream.scannerData.postValue(devices)
        }
    }

    fun loadGateways() {
//        FirebaseAPI.getPublicGateways { gateways ->
//            gatewayStream.gatewayData.postValue(gateways)
//        }
    }

    fun loadMetrics(id: String): LiveData<MetricsMap> {
        val data = MutableLiveData<MetricsMap>()
        GlobalScope.launch {
            val result = repository.getMetrics(MetricsRequest(id, 2, ""))
            metricsStream.metricsData[id] = result
            data.postValue(result)
        }
        return data
    }
}