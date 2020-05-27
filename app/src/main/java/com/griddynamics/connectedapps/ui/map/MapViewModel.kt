package com.griddynamics.connectedapps.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.gateway.network.AirScannerRepository
import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import com.griddynamics.connectedapps.gateway.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.gateway.stream.DeviceStream
import com.griddynamics.connectedapps.model.metrics.MetricsRequest
import com.griddynamics.connectedapps.model.metrics.MetricsResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val deviceStream: DeviceStream,
    private val repository: AirScannerRepository
) : ViewModel() {
    fun loadDevices() {
        FirebaseAPI.getPublicDevices { devices ->
            deviceStream.scannerData.postValue(devices)
        }
    }

    fun loadCo2Metrics(id: String): LiveData<MetricsMap> {
        val data = MutableLiveData<MetricsMap>()
        GlobalScope.launch {
            val result = repository.getMetrics(MetricsRequest(id, 2, "CO2"))
            data.postValue(result)
        }
        return data
    }

    val devices = deviceStream.scannerData

    val metrics = MutableLiveData<MetricsResponse>()
}