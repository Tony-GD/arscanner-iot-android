package com.griddynamics.connectedapps.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.griddynamics.connectedapps.model.User
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.repository.local.LocalStorage
import com.griddynamics.connectedapps.repository.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.util.MapUtil
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val localStorage: LocalStorage) : ViewModel() {
    val user: User
        get() = localStorage.getUser()

    fun loadUserGateways() = FirebaseAPI.getUserGateways(user)
    fun loadUserDevices() = FirebaseAPI.getUserDevices(user)
    fun loadPublicDevices(): LiveData<List<DeviceResponse>> {
        val mediatorLiveData = MediatorLiveData<List<DeviceResponse>>()
        mediatorLiveData.addSource(FirebaseAPI.getPublicDevices()) { devices ->
            mediatorLiveData.value = devices.filter { it.userId != user.uid }
        }
        return mediatorLiveData
    }
}
