package com.griddynamics.connectedapps.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.gateway.api.ApiSuccessResponse
import com.griddynamics.connectedapps.gateway.stream.DeviceStream
import com.griddynamics.connectedapps.model.EMPTY_DEVICES_RESPONSE
import com.griddynamics.connectedapps.model.GetDevicesResponse
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val deviceStream: DeviceStream
) : ViewModel() {

    val devices = deviceStream.scannerData

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}