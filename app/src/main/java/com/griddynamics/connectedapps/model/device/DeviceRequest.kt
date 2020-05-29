package com.griddynamics.connectedapps.model.device

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

const val ADD_DEVICE_REQUEST_KEY = "ADD_DEVICE_REQUEST_KEY"

@Parcelize
data class DeviceRequest(
    @SerializedName("display_name")
    var displayName: String,
    @SerializedName("device_id")
    var deviceId: String,
    @SerializedName("data_format")
    var dataFormat: String,
    @SerializedName("user_id")
    var userId: String,
    @SerializedName("gateway_id")
    var gatewayId: String,
    @SerializedName("loc_lat")
    var locLat: String,
    @SerializedName("loc_lon")
    var locLong: String,
    @SerializedName("loc_desc")
    var locDesc: String
): Parcelable
var EMPTY_DEVICE_REQUEST =
    DeviceRequest(
        "",
        "",
        "",
        "",
        "",
        "json",
        "Empty user id",
        "Empty gateway id"
    )