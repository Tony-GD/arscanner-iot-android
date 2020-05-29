package com.griddynamics.connectedapps.model.device

import com.google.gson.annotations.SerializedName

data class GatewayResponse(
    @SerializedName("gateway_id")
    var gatewayId: String?,
    @SerializedName("user_id")
    var userId: String?,
    @SerializedName("display_name")
    var displayName: String?,
    var key: String?
)

val EMPTY_GATEWAY = GatewayResponse(null, null, null, null)