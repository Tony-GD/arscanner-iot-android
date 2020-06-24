package com.griddynamics.connectedapps.ui.history.week

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.metrics.MetricChartItem
import kotlinx.android.synthetic.main.week_history_chart_item.view.*
import java.text.SimpleDateFormat

private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

class HistoryChartItemsAdapter(
    private val metrics: List<MetricChartItem>,
    private val dateFormat: String = "DD"
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
        val entries = mutableListOf<Entry>()
        metric.data.forEach {
            entries.add(
                Entry(
                    getTimestamp(it.keys.first()).toFloat(),
                    it.values.first().toFloat()
                )
            )
        }
        val yLeft = chartView.axisLeft
        yLeft.textColor = Color.WHITE
        val yRight = chartView.axisRight
        yRight.textColor = Color.TRANSPARENT
        val x = chartView.xAxis
        x.textColor = Color.TRANSPARENT
        val dataSet = LineDataSet(entries, metric.name)
        dataSet.color
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.color = Color.parseColor("#6202EE")
        dataSet.valueTextColor = Color.YELLOW
        dataSet.setDrawFilled(true)
        val lineData = LineData(dataSet)
        chartView.setDrawGridBackground(false)
        chartView.description.text = ""
        chartView.data = lineData
        chartView.invalidate()
    }
}

private fun getDay(date: String, format: String): String {
    return SimpleDateFormat(format)
        .format(
            SimpleDateFormat(DATE_FORMAT)
                .parse(date)
        )
}

private fun getTimestamp(date: String): Long {
    return SimpleDateFormat(DATE_FORMAT)
        .parse(date).time
}

private class CustomDataEntry(private val x: String, private val value: Number) :
    ValueDataEntry(x, value) {
    override fun toString(): String {
        return "CustomDataEntry(x='$x', value=$value)"
    }
}