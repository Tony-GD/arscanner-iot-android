package com.griddynamics.connectedapps.ui.edit.device

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.gateway.network.AirScannerRepository
import com.griddynamics.connectedapps.gateway.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.ui.home.Callback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val CO2 = "co2"
private const val TEMP = "Temp"
private const val HUMIDITY = "Humidity"
private const val PM2_5 = "pm2.5"
private const val PM1_0 = "pm1.0"
private const val PM10 = "pm10"

class EditDeviceViewModel @Inject constructor(
    private val localStorage: LocalStorage,
    private val repository: AirScannerRepository
) :
    ViewModel() {
    var device: DeviceResponse? = null
    var isSingleValue: Boolean = false
    var isAdding = ObservableBoolean()
    var onMapPickerRequest: Callback? = null
    var singleMetricName: String? = null
    var singleMetricMesurement: String? = null
    var isCo2Enabled = ObservableBoolean()
    var isTempEnabled = ObservableBoolean()
    var isHumidityEnabled = ObservableBoolean()
    var isPM2_5Enabled = ObservableBoolean()
    var isPM1_0Enabled = ObservableBoolean()
    var isPM10Enabled = ObservableBoolean()

    val userGateways = MutableLiveData<List<GatewayResponse>>()

    fun loadUserGateways() {
        FirebaseAPI.getUserGateways(localStorage.getUser()) { gateways ->
            userGateways.postValue(gateways)
        }
    }

    fun saveEditedDevice() {
        GlobalScope.launch {
            device?.let {
                if (isAdding.get()) {
                    repository.addDevice(it.apply { saveMetrics() })
                } else {
                    repository.editDevice(it.apply { saveMetrics() })
                }
            }
        }
    }

    private fun DeviceResponse.saveMetrics() {
        if (isSingleValue) {
            if (metrics == null) {
                metrics = mutableMapOf()
            }
            metrics?.put("default", mutableListOf(mutableMapOf(Pair("string", "number"))))
        } else {
            if (jsonMetricsField == null) {
                jsonMetricsField = mutableMapOf()
            }
            if (!isCo2Enabled.get()) {
                jsonMetricsField?.remove(CO2)
            } else {
                jsonMetricsField?.put(CO2, mutableListOf(mutableMapOf(Pair("string", "number"))))
            }
            if (!isTempEnabled.get()) {
                jsonMetricsField?.remove(TEMP)
            } else {
                jsonMetricsField?.put(TEMP, mutableListOf(mutableMapOf(Pair("string", "number"))))
            }
            if (!isHumidityEnabled.get()) {
                jsonMetricsField?.remove(HUMIDITY)
            } else {
                jsonMetricsField?.put(
                    HUMIDITY,
                    mutableListOf(mutableMapOf(Pair("string", "number")))
                )
            }
            if (!isPM2_5Enabled.get()) {
                jsonMetricsField?.remove(PM2_5)
            } else {
                jsonMetricsField?.put(
                    PM2_5,
                    mutableListOf(mutableMapOf(Pair("string", "number")))
                )
            }
            if (!isPM1_0Enabled.get()) {
                jsonMetricsField?.remove(PM1_0)
            } else {
                jsonMetricsField?.put(
                    PM1_0,
                    mutableListOf(mutableMapOf(Pair("string", "number")))
                )
            }
            if (!isPM10Enabled.get()) {
                jsonMetricsField?.remove(PM10)
            } else {
                jsonMetricsField?.put(PM10, mutableListOf(mutableMapOf(Pair("string", "number"))))
            }
        }
    }

    fun deleteDevice() {
        GlobalScope.launch {
            device?.let {
                repository.deleteDevice(it)
            }
        }
    }

}
