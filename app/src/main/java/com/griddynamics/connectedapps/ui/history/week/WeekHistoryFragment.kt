package com.griddynamics.connectedapps.ui.history.week

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.metrics.MetricChartItem
import com.griddynamics.connectedapps.repository.network.api.MetricsMap
import com.griddynamics.connectedapps.repository.network.api.NetworkResponse
import com.griddynamics.connectedapps.ui.history.HistoryViewModel
import com.griddynamics.connectedapps.ui.history.week.events.WeekHistoryEventsStream
import kotlinx.android.synthetic.main.fragment_week_history.*

private const val TAG: String = "WeekHistoryFragment"

class WeekHistoryFragment(
    private val viewModel: HistoryViewModel,
    private val weekHistoryEventsStream: WeekHistoryEventsStream
) : Fragment() {

    private val metrics = mutableListOf<MetricChartItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_week_history, container, false)
    }

    private fun handleLoading() {
        pb_week_loading.visibility = View.VISIBLE
    }

    private fun handleDefault() {
        pb_week_loading.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_week_history_charts.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_week_history_charts.adapter = HistoryChartItemsAdapter(metrics)
    }

    override fun onStart() {
        super.onStart()
        handleLoading()
        viewModel.getWeekMetrics("${viewModel.deviceId}")
            .observe(viewLifecycleOwner, Observer {
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
                        rv_week_history_charts.adapter?.notifyDataSetChanged()
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