package com.griddynamics.connectedapps.ui.settings.devices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.griddynamics.connectedapps.model.settings.SettingsDeviceItem
import com.griddynamics.connectedapps.ui.settings.SettingsViewModel

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
                    val item = SettingsDeviceItem(
                        "${it.deviceId}",
                        SettingsDeviceItem.TYPE_DEVICE,
                        "${it.displayName}",
                        ""
                    )
                    deviceItems.add(item)
                    mediatorLiveData.value = deviceItems
                }
            }

            return mediatorLiveData
        }

    }

    class PublicDevicesLoader(viewModel: SettingsViewModel) : DevicesLoader(viewModel) {
        override fun loadDevices(): LiveData<List<SettingsDeviceItem>> {
            val mediatorLiveData = MediatorLiveData<List<SettingsDeviceItem>>()
            mediatorLiveData.addSource(viewModel.loadPublicDevices()) { response ->
                mediatorLiveData.value = response.map {
                    SettingsDeviceItem(
                        "${it.deviceId}",
                        SettingsDeviceItem.TYPE_DEVICE,
                        "${it.displayName}",
                        ""
                    )
                }
            }
            return mediatorLiveData
        }

    }
}
