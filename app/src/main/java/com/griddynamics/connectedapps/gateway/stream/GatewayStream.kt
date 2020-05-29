package com.griddynamics.connectedapps.gateway.stream

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.model.device.GatewayResponse

interface GatewayStream {
    val gatewayData: MutableLiveData<List<GatewayResponse>>
}