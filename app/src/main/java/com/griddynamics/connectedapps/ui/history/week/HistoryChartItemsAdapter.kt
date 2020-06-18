package com.griddynamics.connectedapps.ui.history.week

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.metrics.MetricChartItem
import kotlinx.android.synthetic.main.week_history_chart_item.view.*
import java.text.SimpleDateFormat

class HistoryChartItemsAdapter(private val metrics: List<MetricChartItem>) :
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
        val anyChartView = holder.view.any_chart_view
        anyChartView.setProgressBar(holder.view.progress_bar_week)

        val cartesian = AnyChart.line()
        cartesian.background().fill("#3C444C")
        cartesian.background().corners(4)
        cartesian.animation(true)

        cartesian.padding(10, 20, 5, 20.0)

        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)

        cartesian.title(metric.name)

        cartesian.yAxis(0).title("Particles")

        val seriesData = ArrayList<DataEntry>()
        var i = 1
        metric.data.forEach {
            seriesData.add(CustomDataEntry("${i++}", it.values.first().toFloat()))
        }

        val set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")

        val series1 = cartesian.line(series1Mapping)
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0, 0, 10, 0.0)

        anyChartView.setChart(cartesian)
    }
}

private fun getDay(date: String): String {
    return SimpleDateFormat("DD").format(SimpleDateFormat("YYYY-MM-DD-"))
}

private class CustomDataEntry(x: String, value: Number) :
    ValueDataEntry(x, value)