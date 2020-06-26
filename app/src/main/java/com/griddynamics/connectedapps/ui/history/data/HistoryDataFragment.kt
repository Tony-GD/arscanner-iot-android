package com.griddynamics.connectedapps.ui.history.data

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.metrics.MetricChartItem
import com.griddynamics.connectedapps.model.settings.hour.HourHistoryItem
import com.griddynamics.connectedapps.repository.network.api.MetricsMap
import com.griddynamics.connectedapps.repository.network.api.NetworkResponse
import com.griddynamics.connectedapps.ui.history.HistoryViewModel
import com.griddynamics.connectedapps.ui.history.data.events.HistoryDataFragmentEventsStream
import kotlinx.android.synthetic.main.fragment_history_data_list.*

private const val TAG: String = "HistoryDataFragment"

class HistoryDataFragment(
    private val timeSpan: String,
    private val viewModel: HistoryViewModel,
    private val historyEventsStream: HistoryDataFragmentEventsStream
) : Fragment() {

    private val metrics = mutableMapOf<String, HourHistoryItem>()
    private val hourMetrics = mutableListOf<MetricChartItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_data_list, container, false)
    }

    private fun handleLoading() {
        tv_hour_empty.visibility = View.GONE
        pb_hour_loading.visibility = View.VISIBLE
        metrics.clear()
        rv_hour_item_metrics.adapter?.notifyDataSetChanged()
    }

    private fun handleDefault() {
        pb_hour_loading.visibility = View.GONE
    }

    private fun handleEmpty() {
        pb_hour_loading.visibility = View.GONE
        tv_hour_empty.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_hour_item_metrics.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_hour_item_metrics.adapter =
            HistoryChartItemsAdapter(
                hourMetrics
            ) {
                viewModel.onMetricSelected(it)
            }
    }

    override fun onStart() {
        super.onStart()
        handleLoading()
        viewModel.getMetricsWithTimeSpan("${viewModel.deviceId}", timeSpan)
            .observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is NetworkResponse.Success<MetricsMap> -> {
                        if (response.body.isNotEmpty()) {
                            handleDefault()
                        } else {
                            handleEmpty()
                        }
                        response.body.keys.forEach { metricName ->
                            hourMetrics.add(MetricChartItem().apply {
                                this.name = metricName
                                response.body[metricName]?.let {
                                    this.data = it
                                }
                            })
                        }
                        rv_hour_item_metrics.adapter?.notifyDataSetChanged()
                    }
                    is NetworkResponse.UnknownError -> {
                        handleEmpty()
                        Log.e(TAG, "WeekHistoryFragment: ", response.error)
                        Toast.makeText(
                            requireContext(),
                            "Unknown error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is NetworkResponse.ApiError<*> -> {
                        handleEmpty()
                        Log.e(TAG, "WeekHistoryFragment: ", RuntimeException("${response.body}"))
                        Toast.makeText(
                            requireContext(),
                            "Error, code: ${response.code}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        Log.e(TAG, "WeekHistoryFragment: $response")
                        Toast.makeText(requireContext(), "Error: $response", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })
    }
}