package com.griddynamics.connectedapps.ui.map.bottomsheet

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.gateway.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.model.DefaultScannersResponse
import com.griddynamics.connectedapps.model.device.DeviceResponse

class BottomSheetDialogViewModel : ViewModel() {
    var device: DeviceResponse? = null
    val metricsMap: MutableMap<String, DefaultScannersResponse> = mutableMapOf()
    fun subscribeForMetrics(device: DeviceResponse): LiveData<DefaultScannersResponse> {
        return FirebaseAPI.subscribeForMetrics("${device.deviceId}")
    }
}