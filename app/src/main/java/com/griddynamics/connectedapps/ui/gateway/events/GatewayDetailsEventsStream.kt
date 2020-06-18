package com.griddynamics.connectedapps.ui.gateway.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.gateway.events.GatewayDetailsScreenEvent

interface GatewayDetailsEventsStream {
    val event: MutableLiveData<GatewayDetailsScreenEvent>
}