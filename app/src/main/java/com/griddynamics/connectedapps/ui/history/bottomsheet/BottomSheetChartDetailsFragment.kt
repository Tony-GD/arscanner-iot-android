package com.griddynamics.connectedapps.ui.history.bottomsheet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.metrics.MetricChartItem
import com.griddynamics.connectedapps.util.getTimestamp
import kotlinx.android.synthetic.main.fragment_chart_bottom.*

class BottomSheetChartDetailsFragment(private val metric: MetricChartItem) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.fragment_chart_bottom, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupChart()
    }

    private fun setupChart() {
        val chartView = bottom_chart
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
        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
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