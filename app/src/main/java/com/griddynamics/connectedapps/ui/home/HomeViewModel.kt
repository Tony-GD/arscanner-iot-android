package com.griddynamics.connectedapps.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.gateway.network.AirScannerGateway
import com.griddynamics.connectedapps.gateway.stream.ScannerStream
import com.griddynamics.connectedapps.model.GetDevicesResponse
import com.griddynamics.connectedapps.model.User
import javax.inject.Inject

private const val TAG: String = "HomeViewModel"

class HomeViewModel @Inject constructor(
    private var gateway: AirScannerGateway,
    private var scannerStream: ScannerStream,
    private val localStorage: LocalStorage
) : ViewModel() {

    private var _devices: LiveData<GetDevicesResponse> = scannerStream.scannerData


    init {
        Log.d(TAG, "init and get name() called ${_devices.value}")
    }

    val devices: LiveData<GetDevicesResponse> = _devices

    val user: LiveData<User>
    get() {
        return MutableLiveData(localStorage.getUser())
    }

    fun load() {
        Log.d(TAG, "load:${scannerStream} ${devices.value}")
        gateway.getAirScanners()
    }
}