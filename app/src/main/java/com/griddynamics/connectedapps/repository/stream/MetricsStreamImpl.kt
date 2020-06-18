package com.griddynamics.connectedapps.repository.stream

import com.google.gson.internal.LinkedTreeMap
import com.griddynamics.connectedapps.repository.network.api.MetricsMap
import javax.inject.Singleton

@Singleton
class MetricsStreamImpl : MetricsStream {
    override val metricsData: MutableMap<String, MetricsMap> = LinkedTreeMap()
}