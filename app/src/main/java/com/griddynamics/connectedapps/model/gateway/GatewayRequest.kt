package com.griddynamics.connectedapps.model.gateway

import com.google.gson.annotations.SerializedName

data class GatewayRequest(
    val key: String?,
    @SerializedName("display_name")
    val displayName: String?
)