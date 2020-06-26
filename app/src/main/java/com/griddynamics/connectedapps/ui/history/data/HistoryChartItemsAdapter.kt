package com.griddynamics.connectedapps.ui.history.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.metrics.MetricChartItem
import com.griddynamics.connectedapps.util.setupChart
import kotlinx.android.synthetic.main.week_history_chart_item.view.*


class HistoryChartItemsAdapter(
    private val metrics: List<MetricChartItem>,
    private val onMetricSelected: (metric: MetricChartItem) -> Unit
) :
    RecyclerView.Adapter<HistoryChartItemsAdapter.WeekHistoryItemViewHolder>() {
    class WeekHistoryItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekHistoryItemViewHolder {
        return WeekHistoryItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.week_history_chart_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return metrics.size
    }

    override fun onBindViewHolder(holder: WeekHistoryItemViewHolder, position: Int) {
        val metric = metrics[position]
        val chartView = holder.view.chart
        setupChart(chartView, metric)
        chartView.setOnLongClickListener {
            showBottomDialog(metric)
            true
        }
    }

    private fun showBottomDialog(metric: MetricChartItem) {
        onMetricSelected(metric)
    }
}