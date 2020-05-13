package com.griddynamics.connectedapps.model

data class SpecialScannerResponse(
    val CO2: Float,
    val Humidity: Float,
    val Temp: Float,
    val PM2_5: Float
)
