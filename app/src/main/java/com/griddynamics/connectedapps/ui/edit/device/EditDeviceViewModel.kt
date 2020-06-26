package com.griddynamics.connectedapps.ui.edit.device

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.google.firebase.firestore.GeoPoint
import com.griddynamics.connectedapps.model.device.*
import com.griddynamics.connectedapps.model.metrics.JsonMetricViewState
import com.griddynamics.connectedapps.repository.local.LocalStorage
import com.griddynamics.connectedapps.repository.network.AirScannerRepository
import com.griddynamics.connectedapps.repository.network.api.NetworkResponse
import com.griddynamics.connectedapps.repository.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.repository.stream.DeviceStream
import com.griddynamics.connectedapps.ui.home.Callback
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEvent
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEventsStream
import com.griddynamics.connectedapps.util.AddressUtil
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
    private val repository: AirScannerRepository,
    private val eventsStream: HomeScreenEventsStream
) :
    ViewModel() {
    var selectedLat: Float = 0f
    var selectedLong: Float = 0f
    var device: DeviceResponse? = null
    val configViewStateList = mutableListOf<JsonMetricViewState>()
    val isSingleValue = ObservableBoolean(false)
    val isAdding = ObservableBoolean()
    var isLoading = ObservableBoolean(false)
    val observableKey = ObservableField<String>()
    var onMapPickerRequest: Callback? = null
    val singleMetricName = ObservableField<String>()
    val isSingleValuePublic = ObservableBoolean()
    val singleMetricMeasurement = ObservableField<String>()
    val networkResponse = MutableLiveData<NetworkResponse<Any, Any>>()

    val userGateways = MediatorLiveData<List<GatewayResponse>>()

    fun getDeviceById(id: String): DeviceResponse {
        lateinit var deviceById: DeviceResponse
        deviceStream.publicDevices.value?.forEach { device ->
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

    private fun loadUserDevices() {
        Log.d(TAG, "loadUserDevices() called")
        deviceStream.userDevices.addSource(FirebaseAPI.getUserDevices(localStorage.getUser()))
        { devices ->
            deviceStream.userDevices.value = devices
        }
    }

    fun setLocationDescription(lat: Double, long: Double): LiveData<String> {
        val mediatorLiveData = MediatorLiveData<String>()
        mediatorLiveData.addSource(AddressUtil.getAddressFrom(GeoPoint(lat, long))) {
            mediatorLiveData.value = it
            device?.locationDescription = it
        }
        return mediatorLiveData
    }

    fun saveEditedDevice() {
        viewModelScope.launch {
            device?.let {
                eventsStream.events.postValue(HomeScreenEvent.LOADING)
                if (isAdding.get()) {
                    val response = repository.addDevice(it.apply { saveMetrics() })
                    eventsStream.events.postValue(HomeScreenEvent.DEFAULT)
                    networkResponse.postValue(response)
                    when (response) {
                        is NetworkResponse.Success<*> -> {
                            Log.d(TAG, "saveAddedDevice success: ${response.body}")
                            loadUserDevices()
                        }
                        else -> Log.e(TAG, "EditDeviceViewModel: error ${response}")
                    }
                } else {
                    val response = repository.editDevice(it.apply { saveMetrics() })
                    eventsStream.events.postValue(HomeScreenEvent.DEFAULT)
                    networkResponse.postValue(response)
                    when (response) {
                        is NetworkResponse.Success<*> -> {
                            Log.d(TAG, "saveEditedDevice success: ${response.body}")
                            loadUserDevices()
                        }
                        else -> Log.e(TAG, "EditDeviceViewModel: error ${response}")
                    }
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
        this.key = observableKey.get()
        if (this.key?.isBlank() == false) {
            this.gatewayId == null
        }
    }

    fun deleteDevice() {
        viewModelScope.launch {
            device?.let {
                repository.deleteDevice("${it.deviceId}")
            }
        }
    }

}
