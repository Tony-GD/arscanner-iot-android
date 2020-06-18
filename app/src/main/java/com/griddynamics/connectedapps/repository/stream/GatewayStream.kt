package com.griddynamics.connectedapps.repository.stream

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.model.device.GatewayResponse

interface GatewayStream {
    val gatewayData: MutableLiveData<List<GatewayResponse>>
}