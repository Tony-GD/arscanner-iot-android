package com.griddynamics.connectedapps.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.griddynamics.connectedapps.model.User
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.model.settings.SettingsDeviceItem
import com.griddynamics.connectedapps.repository.local.LocalStorage
import com.griddynamics.connectedapps.repository.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.repository.stream.GatewayStream
import com.griddynamics.connectedapps.util.AddressUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG: String = "SettingsViewModel"

class SettingsViewModel @Inject constructor(
    private val gatewayStream: GatewayStream,
    private val localStorage: LocalStorage
) : ViewModel() {
    val user: User
        get() = localStorage.getUser()

    fun loadData(): LiveData<List<SettingsDeviceItem>> {
        val mediatorLiveData = MediatorLiveData<List<SettingsDeviceItem>>()
        mediatorLiveData.addSource(loadUserGateways()) { response ->
            mediatorLiveData.value =
                response.toSettingsGatewayItem()
        }
        mediatorLiveData.addSource(loadUserDevices()) { response ->
            mediatorLiveData.value =
                response.toSettingsDeviceItem()
        }
        return mediatorLiveData
    }

    private fun loadUserGateways(): LiveData<List<GatewayResponse>> {
        val mediatorLiveData = MediatorLiveData<List<GatewayResponse>>()
        mediatorLiveData.addSource(FirebaseAPI.getUserGateways(user)) {
            mediatorLiveData.value = it
            gatewayStream.gatewayData.value = it
        }
        return mediatorLiveData
    }

    private fun loadUserDevices() = FirebaseAPI.getUserDevices(user)

    private fun List<GatewayResponse>.toSettingsGatewayItem(): MutableList<SettingsDeviceItem> {
        return ArrayList(this.map {
            SettingsDeviceItem(
                "${it.gatewayId}",
                SettingsDeviceItem.TYPE_GATEWAY,
                "${it.displayName}",
                ""
            )
        })
    }

    private fun List<DeviceResponse>.toSettingsDeviceItem(): MutableList<SettingsDeviceItem> {
        return ArrayList(this.map {
            val device = SettingsDeviceItem(
                "${it.deviceId}", SettingsDeviceItem.TYPE_DEVICE, "${it.displayName}",
                ""
            )
            if (it.locationDescription != null) {
                device.address = "${it.locationDescription}"
            } else {
                it.location?.let { location ->
                    viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            device.address = AddressUtil.getAddress(location)
                        }
                    }
                }
            }
            device
        })
    }
}
