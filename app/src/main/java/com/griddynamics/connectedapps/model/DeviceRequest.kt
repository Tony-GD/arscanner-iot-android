package com.griddynamics.connectedapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

const val ADD_DEVICE_REQUEST_KEY = "ADD_DEVICE_REQUEST_KEY"

@Parcelize
data class DeviceRequest(
    var deviceId: String,
    var key: String,
    var registryId: String,
    var cloudRegion: String,
    var dataFormat: String,
    var userId: String,
    var gatewayId: String,
    var locLat: String,
    var locLong: String,
    var locDesc: String
): Parcelable

var EMPTY_DEVICE_REQUEST = DeviceRequest(
    "Empty device id", "", "",
    "", "", "Empty user id",
    "Empty gateway id", "", "", ""
)