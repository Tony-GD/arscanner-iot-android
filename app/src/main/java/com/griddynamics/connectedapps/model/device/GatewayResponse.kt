package com.griddynamics.connectedapps.model.device

import com.google.gson.annotations.SerializedName

data class GatewayResponse(
    @SerializedName("user_id")
    var userId: String?,
    @SerializedName("display_name")
    var displayName: String?
)