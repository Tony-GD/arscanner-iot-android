package com.griddynamics.connectedapps.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.gateway.network.AirScannerRepository
import com.griddynamics.connectedapps.gateway.stream.DeviceStream
import com.griddynamics.connectedapps.model.User
import com.griddynamics.connectedapps.model.device.DeviceRequest
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.EMPTY_DEVICE_REQUEST
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

typealias DeviceListener = (device: DeviceRequest) -> Unit
private const val TAG: String = "HomeViewModel"

class HomeViewModel @Inject constructor(
    private var repository: AirScannerRepository,
    private var deviceStream: DeviceStream,
    private val localStorage: LocalStorage
) : ViewModel() {

    var onEditListener: DeviceListener? = null


    val devices: LiveData<List<DeviceResponse>> = deviceStream.scannerData

    val user: LiveData<User>
    get() {
        return MutableLiveData(localStorage.getUser())
    }

    fun onRemoveDevice(device: DeviceRequest) {

    }

    fun onEditDevice(device: DeviceRequest) {
        onEditListener?.invoke(device)
    }

    fun load() {
        Log.d(TAG, "load:${deviceStream} ${devices.value}")
    }
}