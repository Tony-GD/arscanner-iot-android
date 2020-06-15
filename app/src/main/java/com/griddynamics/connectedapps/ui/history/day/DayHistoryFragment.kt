package com.griddynamics.connectedapps.ui.history.day

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import com.griddynamics.connectedapps.gateway.network.api.NetworkResponse
import com.griddynamics.connectedapps.model.metrics.MetricChartItem
import com.griddynamics.connectedapps.ui.history.HistoryViewModel
import com.griddynamics.connectedapps.ui.history.week.HistoryItemsAdapter
import kotlinx.android.synthetic.main.fragment_day_history.*

private const val TAG: String = "DayHistoryFragment"

class DayHistoryFragment(private val viewModel: HistoryViewModel) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_day_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metrics = mutableListOf<MetricChartItem>()
        rv_day_history_charts.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_day_history_charts.adapter = HistoryItemsAdapter(metrics)

        viewModel.getDayMetrics("${viewModel.deviceId}")
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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