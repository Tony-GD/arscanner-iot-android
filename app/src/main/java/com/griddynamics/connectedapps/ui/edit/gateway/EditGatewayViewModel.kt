package com.griddynamics.connectedapps.ui.edit.gateway

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.griddynamics.connectedapps.gateway.network.AirScannerRepository
import com.griddynamics.connectedapps.model.device.GatewayResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditGatewayViewModel @Inject constructor(private val repository: AirScannerRepository) :
    ViewModel() {

    val isAdding = ObservableBoolean()

    var gateway: GatewayResponse? = null
    fun saveGateway() {
        viewModelScope.launch {
            gateway?.let {
                repository.addGateway(it)
            }
        }
    }
}
