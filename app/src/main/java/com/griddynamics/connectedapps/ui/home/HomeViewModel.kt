package com.griddynamics.connectedapps.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.gateway.network.AirScannerRepository
import com.griddynamics.connectedapps.model.User
import com.griddynamics.connectedapps.model.device.DeviceRequest
import javax.inject.Inject

typealias DeviceListener = (device: DeviceRequest) -> Unit

private const val TAG: String = "HomeViewModel"

class HomeViewModel @Inject constructor(
    private var repository: AirScannerRepository,
    private val localStorage: LocalStorage
) : ViewModel() {

    val user: LiveData<User>
        get() {
            return MutableLiveData(localStorage.getUser())
        }

}