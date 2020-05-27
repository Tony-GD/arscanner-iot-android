package com.griddynamics.connectedapps.model.metrics

import com.google.gson.annotations.SerializedName

data class MetricsResponse(
    @SerializedName("[default]")
    val DEFALUT: List<Map<String, String>>?,
    val CO2: List<Map<String, String>>?,
    val Temp: List<Map<String, String>>?
)