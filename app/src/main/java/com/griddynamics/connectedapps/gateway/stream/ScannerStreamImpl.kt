package com.griddynamics.connectedapps.gateway.stream

import androidx.lifecycle.MutableLiveData
import javax.inject.Singleton

@Singleton
class ScannerStreamImpl: ScannerStream {
    private  val _scannerData = MutableLiveData("Empty")
    override val scannerData: MutableLiveData<String>
        get() = _scannerData
}