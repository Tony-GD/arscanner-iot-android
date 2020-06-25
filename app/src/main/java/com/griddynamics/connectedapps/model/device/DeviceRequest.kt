package com.griddynamics.connectedapps.model.device

import com.google.gson.annotations.SerializedName

const val ADD_DEVICE_REQUEST_KEY = "ADD_DEVICE_REQUEST_KEY"

data class MetricConfig(
    @SerializedName("is_public") var isPublic: Boolean?,
    @SerializedName("measurementType") var measurementType: String?
)

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
    @SerializedName("location_description")
    var locDesc: String,
    var publicMetrics: List<String>?,
    var metricsConfig: Map<String, MetricConfig>?
)

var EMPTY_DEVICE_REQUEST =
    DeviceRequest(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        emptyList(),
        emptyMap()
    )

