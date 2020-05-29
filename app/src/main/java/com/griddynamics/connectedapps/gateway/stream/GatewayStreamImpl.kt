package com.griddynamics.connectedapps.gateway.stream

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.model.device.GatewayResponse

class GatewayStreamImpl : GatewayStream {
    override val gatewayData: MutableLiveData<List<GatewayResponse>> = MutableLiveData()
}