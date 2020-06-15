package com.griddynamics.connectedapps.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.gateway.network.AirScannerRepository
import com.griddynamics.connectedapps.gateway.network.api.GenericResponse
import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import com.griddynamics.connectedapps.gateway.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.gateway.stream.MetricsStream
import com.griddynamics.connectedapps.model.DefaultScannersResponse
import com.griddynamics.connectedapps.model.metrics.MetricsRequest
import com.griddynamics.connectedapps.model.metrics.TIME_SPAN_LAST_DAY
import com.griddynamics.connectedapps.model.metrics.TIME_SPAN_LAST_HOUR
import com.griddynamics.connectedapps.model.metrics.TIME_SPAN_LAST_WEEK
import com.griddynamics.connectedapps.ui.edit.device.DEFAULT
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val stream: MetricsStream,
    private val repository: AirScannerRepository
) : ViewModel() {
    var deviceId: String? = null
    fun getWeekMetrics(id: String): LiveData<GenericResponse<MetricsMap>> {
        val liveData = MutableLiveData<GenericResponse<MetricsMap>>()
        GlobalScope.launch {
            val data = repository.getMetrics(MetricsRequest("LrzW8eDJbBJu3FN830K4", 2, DEFAULT, TIME_SPAN_LAST_WEEK))
            liveData.postValue(data)
        }
        return liveData
    }

    fun getDayMetrics(id: String): LiveData<GenericResponse<MetricsMap>> {
        val liveData = MutableLiveData<GenericResponse<MetricsMap>>()
        GlobalScope.launch {
            val data = repository.getMetrics(MetricsRequest("LrzW8eDJbBJu3FN830K4", 2, DEFAULT, TIME_SPAN_LAST_DAY))
            liveData.postValue(data)
        }
        return liveData
    }

    fun subscribeForMetrics(deviceId: String): LiveData<DefaultScannersResponse> {
        return FirebaseAPI.subscribeForMetrics("LrzW8eDJbBJu3FN830K4")
    }

    fun getLastHourMetrics(id: String): LiveData<GenericResponse<MetricsMap>> {
        val liveData = MutableLiveData<GenericResponse<MetricsMap>>()
        GlobalScope.launch {
            val data = repository.getMetrics(MetricsRequest("LrzW8eDJbBJu3FN830K4", 2, DEFAULT, TIME_SPAN_LAST_HOUR))
            liveData.postValue(data)
        }
        return liveData
    }

    fun removeDevice(id: String): LiveData<GenericResponse<Any>> {
        val liveData = MutableLiveData<GenericResponse<Any>>()
        GlobalScope.launch {
            val response = repository.deleteDevice(id)
            liveData.postValue(response)
        }
        return liveData
    }
}