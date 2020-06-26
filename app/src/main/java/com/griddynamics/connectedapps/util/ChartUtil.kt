package com.griddynamics.connectedapps.util

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.griddynamics.connectedapps.model.metrics.MetricChartItem

fun setupChart(chartView: LineChart, metric: MetricChartItem) {
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
    dataSet.setDrawCircles(false)
    dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
    dataSet.color = Color.parseColor("#6202EE")
    dataSet.valueTextColor = Color.TRANSPARENT
    dataSet.setDrawFilled(true)
    val lineData = LineData(dataSet)
    chartView.setDrawGridBackground(false)
    chartView.description.text = ""
    chartView.data = lineData
    chartView.invalidate()
}