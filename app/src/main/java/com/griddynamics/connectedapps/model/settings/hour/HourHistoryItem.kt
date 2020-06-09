package com.griddynamics.connectedapps.model.settings.hour

data class HourHistoryItem(
    val title: String,
    val rangeDescription: String?,
    val value: Float,
    val maxValue: Float
)