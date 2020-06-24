package com.griddynamics.connectedapps.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.model.User
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.repository.local.LocalStorage
import com.griddynamics.connectedapps.repository.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.repository.stream.GatewayStream
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val gatewayStream: GatewayStream,
    private val localStorage: LocalStorage
) : ViewModel() {
    val user: User
        get() = localStorage.getUser()

    fun loadUserGateways(): LiveData<List<GatewayResponse>> {
        val mediatorLiveData = MediatorLiveData<List<GatewayResponse>>()
        mediatorLiveData.addSource(FirebaseAPI.getUserGateways(user)) {
            mediatorLiveData.value = it
            gatewayStream.gatewayData.value = it
        }
        return mediatorLiveData
    }

    fun loadUserDevices() = FirebaseAPI.getUserDevices(user)
    fun loadPublicDevices(): LiveData<List<DeviceResponse>> {
        val mediatorLiveData = MediatorLiveData<List<DeviceResponse>>()
        mediatorLiveData.addSource(FirebaseAPI.getPublicDevices()) { devices ->
            mediatorLiveData.value = devices.filter { it.userId != user.uid }
        }
        return mediatorLiveData
    }
}
