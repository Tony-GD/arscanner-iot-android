package com.griddynamics.connectedapps.gateway.stream

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.model.GetDevicesResponse

interface ScannerStream {
    val scannerData: MutableLiveData<GetDevicesResponse>
}