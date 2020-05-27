package com.griddynamics.connectedapps.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.griddynamics.connectedapps.gateway.network.AirScannerRepository
import com.griddynamics.connectedapps.model.device.DeviceRequest
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.ui.home.Callback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditDeviceViewModel @Inject constructor(private val repository: AirScannerRepository) : ViewModel() {
    var device: DeviceResponse? = null
    var deviceRequest: DeviceRequest? = null
    var onMapPickerRequest: Callback? = null
    val editDevice = liveData<Any> {
        deviceRequest?.let {
            val response = ""
            emit(response)
        }
    }

    fun saveEditedDevice() {
        GlobalScope.launch {
            device?.let {
                repository.editDevice(it)
            }
        }
    }

}
