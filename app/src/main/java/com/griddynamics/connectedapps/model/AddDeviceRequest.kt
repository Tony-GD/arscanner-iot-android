package com.griddynamics.connectedapps.model

data class AddDeviceRequest(
    val deviceId: String,
    val key: String,
    val registryId: String,
    val cloudRegion: String,
    val dataFormat: String,
    val userId: String,
    val gatewayId: String,
    val locLat: String,
    val locLong: String,
    val locDesc: String
)