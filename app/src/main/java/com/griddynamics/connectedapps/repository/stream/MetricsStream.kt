package com.griddynamics.connectedapps.repository.stream

import com.griddynamics.connectedapps.repository.network.api.MetricsMap

interface MetricsStream {
    val metricsData: MutableMap<String, MetricsMap>
}