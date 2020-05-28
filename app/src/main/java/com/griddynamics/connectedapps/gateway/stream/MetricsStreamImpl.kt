package com.griddynamics.connectedapps.gateway.stream

import androidx.lifecycle.MutableLiveData
import com.google.gson.internal.LinkedTreeMap
import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import javax.inject.Singleton

@Singleton
class MetricsStreamImpl : MetricsStream {
    override val metricsData: MutableMap<String, MetricsMap> = LinkedTreeMap()
}