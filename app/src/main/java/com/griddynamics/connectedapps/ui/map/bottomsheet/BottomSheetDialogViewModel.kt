package com.griddynamics.connectedapps.ui.map.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.repository.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.model.DefaultScannersResponse
import com.griddynamics.connectedapps.model.device.DeviceResponse

private const val TAG: String = "BottomSheetDialogViewMo"

class BottomSheetDialogViewModel : ViewModel() {
    var device: DeviceResponse? = null
    val metricsMap: MutableMap<String, DefaultScannersResponse> = mutableMapOf()
    fun subscribeForMetrics(device: DeviceResponse): LiveData<DefaultScannersResponse> {
        return FirebaseAPI.subscribeForMetrics("${device.deviceId}")
    }
}