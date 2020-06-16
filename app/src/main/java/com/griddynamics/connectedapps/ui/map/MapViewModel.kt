package com.griddynamics.connectedapps.ui.map

import android.util.Log
import androidx.lifecycle.*
import com.griddynamics.connectedapps.gateway.local.LocalStorage
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
import com.griddynamics.connectedapps.ui.map.filter.FilterViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG: String = "MapViewModel"

class MapViewModel @Inject constructor(
    private val localStorage: LocalStorage,
    private val deviceStream: DeviceStream,
    private val gatewayStream: GatewayStream,
    private val metricsStream: MetricsStream,
    private val repository: AirScannerRepository
) : ViewModel() {
    val filterViewState = FilterViewState()
    val devices = MediatorLiveData<List<DeviceResponse>>()
    val gateways = MediatorLiveData<List<GatewayResponse>>()

    fun loadDevices() {
        devices.addSource(
            FirebaseAPI.getPublicDevices()
        ) { devices ->
            Log.d(TAG, "loadDevices() publicDevices called with: devices = [$devices]")
            this.devices.value = devices
            deviceStream.publicDevices.postValue(devices)
        }
        deviceStream.userDevices.addSource(
            FirebaseAPI.getUserDevices(localStorage.getUser())
        ) { devices ->
            Log.d(TAG, "loadDevices() userDevices called with: devices = [$devices]")
            deviceStream.userDevices.value = devices
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
        viewModelScope.launch {
            when (val result = repository.getMetrics(MetricsRequest(id, 2, "", "lastHour"))) {
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