package com.griddynamics.connectedapps.gateway.stream

import androidx.lifecycle.MutableLiveData

interface ScannerStream {
    val scannerData: MutableLiveData<String>
}