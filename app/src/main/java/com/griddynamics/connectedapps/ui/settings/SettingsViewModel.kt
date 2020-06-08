package com.griddynamics.connectedapps.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.gateway.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.model.User
import com.griddynamics.connectedapps.util.MapUtil
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val localStorage: LocalStorage) : ViewModel() {
    val user: User
        get() = localStorage.getUser()

    fun loadUserGateways() = FirebaseAPI.getUserGateways(user)
    fun loadUserDevices() = FirebaseAPI.getUserDevices(user)
    fun loadAddress(location: GeoPoint?): LiveData<String> = if (location != null) {
        MapUtil.getAddressFrom(location)
    } else {
        MutableLiveData()
    }
}
