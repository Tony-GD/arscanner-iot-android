package com.griddynamics.connectedapps.model

import com.griddynamics.connectedapps.ui.edit.device.CO2
import com.griddynamics.connectedapps.ui.edit.device.HUMIDITY
import com.griddynamics.connectedapps.ui.edit.device.PM2_5
import com.griddynamics.connectedapps.ui.edit.device.TEMP

const val SCANNER_RESPONSE_ID = "SCANNER_RESPONSE_ID"

data class DefaultScannersResponse(var metricName: String, var value: Float) {
    val maxValue: Float
    get() {
        return when  {
            metricName.contains(TEMP, true) -> 100f
            metricName.contains(CO2, true) -> 1000f
            metricName.contains(HUMIDITY, true) -> 100f
            metricName.contains(PM2_5, true) -> 10f
            else -> value * 1.3f
        }
    }
}