package com.griddynamics.connectedapps.gateway.stream

import com.griddynamics.connectedapps.gateway.network.api.MetricsMap

interface MetricsStream {
    val metricsData: MutableMap<String, MetricsMap>
}