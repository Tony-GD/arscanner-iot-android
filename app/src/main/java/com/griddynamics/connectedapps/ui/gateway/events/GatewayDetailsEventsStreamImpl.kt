package com.griddynamics.connectedapps.ui.gateway.events

import androidx.lifecycle.MutableLiveData

class GatewayDetailsEventsStreamImpl: GatewayDetailsEventsStream {
    override val event: MutableLiveData<GatewayDetailsScreenEvent> = MutableLiveData()
}