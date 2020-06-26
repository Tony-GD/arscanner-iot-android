package com.griddynamics.connectedapps.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.griddynamics.connectedapps.model.DefaultScannersResponse
import com.griddynamics.connectedapps.model.metrics.*
import com.griddynamics.connectedapps.repository.local.LocalStorage
import com.griddynamics.connectedapps.repository.network.AirScannerRepository
import com.griddynamics.connectedapps.repository.network.api.GenericResponse
import com.griddynamics.connectedapps.repository.network.api.MetricsMap
import com.griddynamics.connectedapps.repository.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.repository.stream.DeviceStream
import com.griddynamics.connectedapps.repository.stream.MetricsStream
import com.griddynamics.connectedapps.ui.home.edit.device.DEFAULT
import com.griddynamics.connectedapps.ui.history.events.HistoryFragmentEvent
import com.griddynamics.connectedapps.ui.history.events.HistoryFragmentEventsStream
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val stream: MetricsStream,
    private val deviceStream: DeviceStream,
    private val repository: AirScannerRepository,
    private val localStorage: LocalStorage,
    private val historyFragmentEventsStream: HistoryFragmentEventsStream
) : ViewModel() {
    var deviceId: String? = null

    val isEditable: Boolean
        get() {
            return deviceStream.publicDevices.value?.firstOrNull() {
                (it.deviceId == deviceId && it.userId == localStorage.getUser().uid)
            } != null
        }

    fun getWeekMetrics(id: String): LiveData<GenericResponse<MetricsMap>> {
        val liveData = MutableLiveData<GenericResponse<MetricsMap>>()
        viewModelScope.launch {
            val data = repository.getMetrics(MetricsRequest(id, 2, DEFAULT, TIME_SPAN_LAST_WEEK))
            liveData.postValue(data)
        }
        return liveData
    }

    fun getDayMetrics(id: String): LiveData<GenericResponse<MetricsMap>> {
        val liveData = MutableLiveData<GenericResponse<MetricsMap>>()
        viewModelScope.launch {
            val data = repository.getMetrics(
                MetricsRequest(
                    id,//"LrzW8eDJbBJu3FN830K4",
                    2,
                    DEFAULT,
                    TIME_SPAN_LAST_DAY
                )
            )
            liveData.postValue(data)
        }
        return liveData
    }

    fun subscribeForMetrics(deviceId: String): LiveData<DefaultScannersResponse> {
        return FirebaseAPI.subscribeForMetrics(deviceId)
    }

    fun getLastHourMetrics(id: String): LiveData<GenericResponse<MetricsMap>> {
        val liveData = MutableLiveData<GenericResponse<MetricsMap>>()
        viewModelScope.launch {
            val data = repository.getMetrics(
                MetricsRequest(
                    id,
                    2,
                    DEFAULT,
                    TIME_SPAN_LAST_HOUR
                )
            )
            liveData.postValue(data)
        }
        return liveData
    }

    fun getMetricsWithTimeSpan(id: String, timeSpan: String): LiveData<GenericResponse<MetricsMap>> {
        val liveData = MutableLiveData<GenericResponse<MetricsMap>>()
        viewModelScope.launch {
            val data = repository.getMetrics(
                MetricsRequest(
                    id,
                    2,
                    DEFAULT,
                    timeSpan
                )
            )
            liveData.postValue(data)
        }
        return liveData
    }

    fun removeDevice(id: String): LiveData<GenericResponse<Any>> {
        val liveData = MutableLiveData<GenericResponse<Any>>()
        viewModelScope.launch {
            val response = repository.deleteDevice(id)
            liveData.postValue(response)
        }
        return liveData
    }

    fun onMetricSelected(metric: MetricChartItem) {
        historyFragmentEventsStream.events.postValue(
            HistoryFragmentEvent.ShowBottomDialogEvent(
                metric
            )
        )
    }

    fun addToWidget() {
        localStorage.saveWidgetTrackedDevice("$deviceId")
    }
}