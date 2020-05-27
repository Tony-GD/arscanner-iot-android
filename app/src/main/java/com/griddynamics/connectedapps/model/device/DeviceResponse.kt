package com.griddynamics.connectedapps.model.device

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.gson.annotations.SerializedName

data class DeviceResponse(
    @SerializedName("device_id")
    var deviceId: String?,
    @SerializedName("data_format")
    var dataFormat: String?,
    @SerializedName("updated_at")
    var updateAt: Timestamp?,
    @SerializedName("user_id")
    var userId: String?,
    @SerializedName("created_at")
    var createdAt: Timestamp?,
    var location: GeoPoint?,
    @SerializedName("display_name")
    var displayName: String?,
    @SerializedName("location_description")
    var locationDescription: String?,
    @SerializedName("gateway_id")
    var gatewayId: String?
)