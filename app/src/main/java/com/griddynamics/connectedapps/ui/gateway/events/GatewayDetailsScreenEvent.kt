package com.griddynamics.connectedapps.ui.gateway.events

sealed class GatewayDetailsScreenEvent {
    class NavigateToDeviceDetailsScreenEvent(val deviceId: String, val address: String) : GatewayDetailsScreenEvent()
    object ReturnDefaultStateEvent: GatewayDetailsScreenEvent()
}