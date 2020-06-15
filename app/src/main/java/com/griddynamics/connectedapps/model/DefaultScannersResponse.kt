package com.griddynamics.connectedapps.model

import com.griddynamics.connectedapps.ui.edit.device.*

const val SCANNER_RESPONSE_ID = "SCANNER_RESPONSE_ID"

data class DefaultScannersResponse(var metricName: String, var value: Float) {
    val maxValue: Float
    get() {
        return when  {
            metricName.contains(TEMP_SHORT, true) -> 100f
            metricName.contains(CO2, true) -> 1000f
            metricName.contains(HUMIDITY, true) -> 100f
            metricName.contains(PM2, true) -> 10f
            metricName.contains(PM1, true) -> 100f
            else -> value * 1.3f
        }
    }
}