package com.griddynamics.connectedapps.ui.gateway.events

sealed class GatewayDetailsScreenEvent {
    class NavigateToDeviceDetailsScreenEvent(val deviceId: String, val address: String) : GatewayDetailsScreenEvent()
    object DEFAULT_STATE_EVENT: GatewayDetailsScreenEvent()
    object SUCCESS: GatewayDetailsScreenEvent()
    class Error(val message: String?): GatewayDetailsScreenEvent()
}