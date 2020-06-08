package com.griddynamics.connectedapps.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.gateway.network.AirScannerRepository
import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import com.griddynamics.connectedapps.gateway.network.api.NetworkResponse
import com.griddynamics.connectedapps.gateway.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.gateway.stream.DeviceStream
import com.griddynamics.connectedapps.gateway.stream.GatewayStream
import com.griddynamics.connectedapps.gateway.stream.MetricsStream
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.model.metrics.MetricsRequest
import com.griddynamics.connectedapps.model.metrics.MetricsResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG: String = "MapViewModel"

class MapViewModel @Inject constructor(
    private val deviceStream: DeviceStream,
    private val gatewayStream: GatewayStream,
    private val metricsStream: MetricsStream,
    private val repository: AirScannerRepository
) : ViewModel() {
    val devices = MediatorLiveData<List<DeviceResponse>>()
    val gateways = MediatorLiveData<List<GatewayResponse>>()
    val metrics = MutableLiveData<MetricsResponse>()

    fun loadDevices() {
        devices.addSource(
            FirebaseAPI.getPublicDevices()
        ) { devices ->
            Log.d(TAG, "loadDevices: ")
            this.devices.value = devices
            deviceStream.scannerData.postValue(devices)
        }
    }

    fun loadGateways() {
        gateways.addSource(
            FirebaseAPI.getPublicGateways()
        ) { gateways ->
            this.gateways.value = gateways
            gatewayStream.gatewayData.postValue(gateways)
        }
    }

    fun loadMetrics(id: String): LiveData<MetricsMap> {
        val data = MutableLiveData<MetricsMap>()
        GlobalScope.launch {
            when (val result = repository.getMetrics(MetricsRequest(id, 2, ""))) {
                is NetworkResponse.Success<MetricsMap> -> {
                    metricsStream.metricsData[id] = result.body
                    data.postValue(result.body)
                }
                else -> {
                    Log.e(TAG, "MapViewModel: $result")
                }
            }
        }
        return data
    }
}