package com.griddynamics.connectedapps.ui.edit.device

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.gateway.network.AirScannerRepository
import com.griddynamics.connectedapps.gateway.network.api.NetworkResponse
import com.griddynamics.connectedapps.gateway.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.gateway.stream.DeviceStream
import com.griddynamics.connectedapps.model.device.*
import com.griddynamics.connectedapps.model.metrics.JsonMetricViewState
import com.griddynamics.connectedapps.ui.home.Callback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

const val CO2 = "CO2"
const val TEMP = "Temperature"
const val TEMP_SHORT = "Temp"
const val HUMIDITY = "Humidity"
const val PM2_5 = "PM2,5"
const val PM2 = "pm2"
const val PM1_0 = "PM1,0"
const val PM1 = "pm1"
const val PM10 = "PM10"
const val DEFAULT = "[default]"

private const val TAG: String = "EditDeviceViewModel"

class EditDeviceViewModel @Inject constructor(
    private val deviceStream: DeviceStream,
    private val localStorage: LocalStorage,
    private val repository: AirScannerRepository
) :
    ViewModel() {
    var device: DeviceResponse? = null
    val configViewStateList = mutableListOf<JsonMetricViewState>()
    val isSingleValue = ObservableBoolean(false)
    val isAdding = ObservableBoolean()
    var onMapPickerRequest: Callback? = null
    val singleMetricName = ObservableField<String>()
    val isSingleValuePublic = ObservableBoolean()
    val singleMetricMeasurement = ObservableField<String>()
    val networkResponse = MutableLiveData<NetworkResponse<Any, Any>>()

    val userGateways = MediatorLiveData<List<GatewayResponse>>()

    fun getDeviceById(id: String): DeviceResponse {
        lateinit var deviceById: DeviceResponse
        deviceStream.scannerData.value?.forEach { device->
            if (device.deviceId == id) {
                deviceById = device
            }
        }
        return deviceById
    }

    fun loadUserGateways() {
        userGateways.addSource(FirebaseAPI.getUserGateways(localStorage.getUser()))
        { gateways ->
            userGateways.value = gateways
        }
    }

    fun saveEditedDevice() {
        GlobalScope.launch {
            device?.let {
                if (isAdding.get()) {
                    val response = repository.addDevice(it.apply { saveMetrics() })
                    networkResponse.postValue(response)
                    when (response) {
                        is NetworkResponse.Success<*> ->
                            Log.d(TAG, "saveEditedDevice success: ${response.body}")
                        else -> Log.e(TAG, "EditDeviceViewModel: error ${response}")
                    }
                } else {
                    repository.editDevice(it.apply { saveMetrics() })
                }
            }
        }
    }

    private fun DeviceResponse.saveMetrics() {

        val tempMetricsConfig = mutableMapOf<String, MetricConfig>()
        val tempPublicMetrics = mutableListOf<String>()
        if (isSingleValue.get()) {
            tempMetricsConfig[singleMetricName.get() ?: DEFAULT] =
                MetricConfig(isSingleValuePublic.get(), "${singleMetricMeasurement.get()}")
            if (isSingleValuePublic.get()) {
                tempPublicMetrics += "${singleMetricName.get()}"
            }
        } else {
            configViewStateList.forEach { config ->
                tempMetricsConfig["${config.name.get()}"] =
                    MetricConfig(config.isPublic.get(), "${config.measurement.get()}")
                if (config.isPublic.get()) {
                    tempPublicMetrics += "${config.name.get()}"
                }
            }
        }
        this.dataFormat = if (isSingleValue.get()) METRIC_TYPE_SINGLE else METRIC_TYPE_JSON
        this.metricsConfig = tempMetricsConfig
        this.publicMetrics = tempPublicMetrics
    }

    fun deleteDevice() {
        GlobalScope.launch {
            device?.let {
                repository.deleteDevice("${it.deviceId}")
            }
        }
    }

}
