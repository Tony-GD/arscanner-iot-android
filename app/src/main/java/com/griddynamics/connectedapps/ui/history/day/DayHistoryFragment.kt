package com.griddynamics.connectedapps.ui.history.day

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.metrics.MetricChartItem
import com.griddynamics.connectedapps.repository.network.api.MetricsMap
import com.griddynamics.connectedapps.repository.network.api.NetworkResponse
import com.griddynamics.connectedapps.ui.history.HistoryViewModel
import com.griddynamics.connectedapps.ui.history.day.events.DayHistoryEventsStream
import com.griddynamics.connectedapps.ui.history.week.HistoryChartItemsAdapter
import kotlinx.android.synthetic.main.fragment_day_history.*

private const val TAG: String = "DayHistoryFragment"

class DayHistoryFragment(
    private val viewModel: HistoryViewModel,
    private val dayHistoryEventsStream: DayHistoryEventsStream
) : Fragment() {

    private val metrics = mutableListOf<MetricChartItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_day_history, container, false)
    }

    private fun handleLoading() {
        metrics.clear()
        rv_day_history_charts.adapter?.notifyDataSetChanged()
        pb_day_loading.visibility = View.VISIBLE
    }

    private fun handleDefault() {
        pb_day_loading.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_day_history_charts.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_day_history_charts.adapter = HistoryChartItemsAdapter(metrics, "HH")
    }

    override fun onStart() {
        super.onStart()
        handleLoading()
        viewModel.getDayMetrics("${viewModel.deviceId}")
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                handleDefault()
                when (it) {
                    is NetworkResponse.Success<MetricsMap> -> {
                        Log.d(TAG, "onViewCreated: result ${it.body}")
                        it.body.keys.forEach { metricName ->
                            metrics.add(MetricChartItem().apply {
                                this.name = metricName
                                it.body[metricName]?.let {
                                    this.data = it
                                }
                            })
                        }
                        rv_day_history_charts.adapter?.notifyDataSetChanged()
                    }
                    is NetworkResponse.UnknownError -> {
                        Log.e(TAG, "WeekHistoryFragment: ", it.error)
                    }
                    else -> {
                        Log.e(TAG, "WeekHistoryFragment: $it")
                    }
                }
            })
    }
}