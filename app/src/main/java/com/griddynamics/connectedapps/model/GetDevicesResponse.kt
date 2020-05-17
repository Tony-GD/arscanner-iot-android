package com.griddynamics.connectedapps.model

data class GetDevicesResponse(val devices: List<AddDeviceRequest>)

val EMPTY_DEVICES_RESPONSE = GetDevicesResponse(emptyList())