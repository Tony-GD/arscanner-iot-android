package com.griddynamics.connectedapps.gateway.stream

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.model.EMPTY_DEVICES_RESPONSE
import com.griddynamics.connectedapps.model.GetDevicesResponse
import javax.inject.Singleton

@Singleton
class ScannerStreamImpl : ScannerStream {
    private val _scannerData by lazy { MutableLiveData(EMPTY_DEVICES_RESPONSE) }
    override val scannerData: MutableLiveData<GetDevicesResponse>
        get() = _scannerData
}