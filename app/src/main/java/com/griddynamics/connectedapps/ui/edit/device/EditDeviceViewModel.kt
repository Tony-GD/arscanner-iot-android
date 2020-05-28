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

class EditDeviceViewModel @Inject constructor(private val localStorage: LocalStorage, private val repository: AirScannerRepository) :
    ViewModel() {
    var device: DeviceResponse? = null
    var isSingleValue: Boolean = false
    var isAdding = ObservableBoolean()
    var onMapPickerRequest: Callback? = null
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
                    repository.addDevice(it)
                } else {
                    repository.editDevice(it)
                }
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
