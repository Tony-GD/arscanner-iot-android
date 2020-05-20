package com.griddynamics.connectedapps.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.gateway.api.ApiResponse
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.gateway.network.AirScannerGateway
import com.griddynamics.connectedapps.gateway.stream.DeviceStream
import com.griddynamics.connectedapps.model.DeviceRequest
import com.griddynamics.connectedapps.model.GetDevicesResponse
import com.griddynamics.connectedapps.model.User
import javax.inject.Inject

typealias DeviceListener = (device: DeviceRequest) -> Unit
private const val TAG: String = "HomeViewModel"

class HomeViewModel @Inject constructor(
    private var gateway: AirScannerGateway,
    private var deviceStream: DeviceStream,
    private val localStorage: LocalStorage
) : ViewModel() {

    var onEditListener: DeviceListener? = null

    private var _devices: LiveData<ApiResponse<GetDevicesResponse>> = deviceStream.scannerData

    val devices: LiveData<ApiResponse<GetDevicesResponse>> = _devices

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
        gateway.getAirScanners()
    }
}