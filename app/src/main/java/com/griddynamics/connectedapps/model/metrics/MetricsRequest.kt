package com.griddynamics.connectedapps.model.metrics

const val TIME_SPAN_LAST_HOUR = "lastHour"
const val TIME_SPAN_LAST_DAY = "lastDay"
const val TIME_SPAN_LAST_WEEK = "lastWeek"
data class MetricsRequest(val id: String, val smooth: Int, val metrics: String, val timeSpan: String)