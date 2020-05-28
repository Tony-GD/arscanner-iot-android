package com.griddynamics.connectedapps.ui.history

import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import com.griddynamics.connectedapps.gateway.stream.MetricsStream
import javax.inject.Inject

class HistoryFragmentViewModel @Inject constructor(private val stream: MetricsStream) : ViewModel() {
    fun getMetrics(id: String): MetricsMap? {
        return stream.metricsData[id]
    }
}