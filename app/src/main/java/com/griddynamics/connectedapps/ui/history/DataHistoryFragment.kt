package com.griddynamics.connectedapps.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.ui.edit.device.EditDeviceFragmentArgs
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_data_history.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

private const val TAG: String = "DataHistoryFragment"

class DataHistoryFragment : DaggerFragment() {
    private lateinit var viewModel: HistoryFragmentViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_data_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[HistoryFragmentViewModel::class.java]
        val anyChartView: AnyChartView = acv_data_view
        anyChartView.setProgressBar(progress_bar)
        val deviceId = EditDeviceFragmentArgs.fromBundle(arguments).device
        displayChart(deviceId, anyChartView)
    }

    private fun displayChart(
        deviceId: String,
        anyChartView: AnyChartView
    ) {
        val data = ArrayList<DataEntry>()
        data.add(ValueDataEntry("metricDateKey", 33))
        data.add(ValueDataEntry("metricDateKey1", 23))
        data.add(ValueDataEntry("metricDateKey2", 43))
        data.add(ValueDataEntry("metricDateKey3", 35))
        data.add(ValueDataEntry("metricDateKey4", 38))
        data.add(ValueDataEntry("metricDateKey5", 40))

//        val metrics = viewModel.getMetrics(deviceId)
//        metrics?.keys?.firstOrNull()?.let { metricName ->//take first metric section
//            metrics[metricName]?.firstOrNull()?.let { metric ->//iterate through the section
//                metric.keys.forEach { metricDateKey ->//  get key and value
//                    val rand = Random()
//                    val n: Int = rand.nextInt(50)
//                    data.add(ValueDataEntry(metricDateKey, n))
//                }
//            }
            showData(data, deviceId, "default", anyChartView)
//        }
    }

    private fun showData(
        data: MutableList<DataEntry>,
        deviceId: String,
        metricName: String,
        anyChartView: AnyChartView
    ) {
        Log.d(TAG, "showData() called with: data = [$data], deviceId = [$deviceId], metricName = [$metricName], anyChartView = [$anyChartView]")
        val cartesian: Cartesian = AnyChart.column()
        val column: Column = cartesian.column(data)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("{%Value}{groupsSeparator: }")

        cartesian.animation(true)
        cartesian.title("Device: ${deviceId}")

        cartesian.yScale().minimum(0.0)

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Time")
        cartesian.yAxis(0).title("$metricName metric")
Toast.makeText(requireContext(), "devices", Toast.LENGTH_SHORT).show()
        anyChartView.setChart(cartesian)
    }
}
