package com.griddynamics.connectedapps.ui.history.events

import com.griddynamics.connectedapps.model.metrics.MetricChartItem

sealed class HistoryFragmentEvent {
    object DEFAULT: HistoryFragmentEvent()
    class ShowBottomDialogEvent(val metricChartItem: MetricChartItem): HistoryFragmentEvent()
}