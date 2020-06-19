package com.griddynamics.connectedapps.ui.edit.gateway

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.repository.network.AirScannerRepository
import com.griddynamics.connectedapps.repository.network.api.NetworkResponse
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEvent
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEventsStream
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditGatewayViewModel @Inject constructor(
    private val repository: AirScannerRepository,
    private val eventsStream: HomeScreenEventsStream
) :
    ViewModel() {

    val isLoading = ObservableBoolean(false)

    val isAdding = ObservableBoolean()

    var gateway: GatewayResponse? = null

    var key: String?
        get() = gateway?.key
        set(value) {
            gateway?.apply {
                if (value?.isNotBlank() == true) {
                    key = value.toString()
                }
            }
        }

    var name: String?
        get() = gateway?.displayName
        set(value) {
            gateway?.apply {
                if (value?.isNotBlank() == true) {
                    displayName = value.toString()
                }
            }
        }

    fun saveGateway() {
        isLoading.set(true)
        viewModelScope.launch {
            gateway?.let {
                val result = repository.addGateway(it)
                isLoading.set(false)
                if (result is NetworkResponse.Success) {
                    eventsStream.events.postValue(HomeScreenEvent.SUCCESS)
                } else {
                    eventsStream.events.postValue(HomeScreenEvent.ERROR)
                }
            }
        }
    }
}
