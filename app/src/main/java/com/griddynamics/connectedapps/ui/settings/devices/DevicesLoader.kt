package com.griddynamics.connectedapps.ui.settings.devices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.griddynamics.connectedapps.model.settings.SettingsDeviceItem
import com.griddynamics.connectedapps.ui.settings.SettingsViewModel
import com.griddynamics.connectedapps.util.MapUtil.getAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG: String = "DevicesLoader"

sealed class DevicesLoader(protected val viewModel: SettingsViewModel) {
    abstract fun loadDevices(): LiveData<List<SettingsDeviceItem>>
    class UserDevicesLoader(viewModel: SettingsViewModel) : DevicesLoader(viewModel) {
        override fun loadDevices(): LiveData<List<SettingsDeviceItem>> {
            val deviceItems = mutableListOf<SettingsDeviceItem>()
            val mediatorLiveData = MediatorLiveData<List<SettingsDeviceItem>>()
            mediatorLiveData.addSource(viewModel.loadUserGateways()) { gateways ->
                gateways.forEach {
                    deviceItems.add(
                        SettingsDeviceItem(
                            "${it.gatewayId}",
                            SettingsDeviceItem.TYPE_GATEWAY,
                            "${it.displayName}",
                            ""
                        )
                    )
                }
                mediatorLiveData.value = deviceItems
            }
            mediatorLiveData.addSource(viewModel.loadUserDevices()) { devices ->
                devices.forEach {
                    viewModel.viewModelScope.launch {
                        val item = SettingsDeviceItem(
                            "${it.deviceId}",
                            SettingsDeviceItem.TYPE_DEVICE,
                            "${it.displayName}",
                            ""
                        )
                        withContext(Dispatchers.IO) {
                            it.location?.let { location ->
                                item.address = getAddress(location)
                            }
                        }
                        withContext(Dispatchers.Main) {
                            deviceItems.add(item)
                            mediatorLiveData.value = deviceItems
                        }
                    }
                }
            }

            return mediatorLiveData
        }

    }

    class PublicDevicesLoader(viewModel: SettingsViewModel) : DevicesLoader(viewModel) {
        override fun loadDevices(): LiveData<List<SettingsDeviceItem>> {
            val mediatorLiveData = MediatorLiveData<List<SettingsDeviceItem>>()
            mediatorLiveData.addSource(viewModel.loadPublicDevices()) { response ->
                viewModel.viewModelScope.launch {
                    val value = response.map {
                        val item = SettingsDeviceItem(
                            "${it.deviceId}",
                            SettingsDeviceItem.TYPE_DEVICE,
                            "${it.displayName}",
                            ""
                        )
                        withContext(Dispatchers.IO) {
                            it.location?.let { location ->
                                item.address = getAddress(location)
                            }
                        }
                        item
                    }
                    withContext(Dispatchers.Main) {
                        mediatorLiveData.value = value
                    }
                }
            }
            return mediatorLiveData
        }

    }
}
