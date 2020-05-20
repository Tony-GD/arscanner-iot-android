package com.griddynamics.connectedapps.model

data class GetDevicesResponse(val devices: List<DeviceRequest>)
private val DEFAULT_LAT = "49.9883581"
private val DEFAULT_LONG = "36.332845"
private val DEFAULT_LONG_2 = "36.532846"
private val DEFAULT_LONG_3 = "36.632847"
val EMPTY_DEVICES_RESPONSE = GetDevicesResponse(listOf(
    EMPTY_DEVICE_REQUEST.copy(locLat = DEFAULT_LAT, locLong = DEFAULT_LONG),
    EMPTY_DEVICE_REQUEST.copy(userId = "user2", locLat = DEFAULT_LAT, locLong = DEFAULT_LONG_2),
    EMPTY_DEVICE_REQUEST.copy(userId = "user3", locLat = DEFAULT_LAT, locLong = DEFAULT_LONG_3)))