package com.griddynamics.connectedapps.ui.history.hour

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
import com.griddynamics.connectedapps.ui.history.hour.events.HourHistoryEventsStream
import com.griddynamics.connectedapps.ui.history.week.HistoryChartItemsAdapter
import kotlinx.android.synthetic.main.fragmnet_hour_history.*

private const val TAG: String = "HourHistoryFragment"

class HourHistoryFragment(
    private val viewModel: HistoryViewModel,
    private val historyEventsStream: HourHistoryEventsStream
) : Fragment() {

    private val metrics = mutableMapOf<String, HourHistoryItem>()
    private val hourMetrics = mutableListOf<MetricChartItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmnet_hour_history, container, false)
    }

    private fun handleLoading() {
        pb_hour_loading.visibility = View.VISIBLE
        metrics.clear()
        rv_hour_item_metrics.adapter?.notifyDataSetChanged()
    }

    private fun handleDefault() {
        pb_hour_loading.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_hour_item_metrics.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_hour_item_metrics.adapter = HistoryChartItemsAdapter(hourMetrics, "mm")
    }

    override fun onStart() {
        super.onStart()
        handleLoading()
        viewModel.getLastHourMetrics("${viewModel.deviceId}")
            .observe(viewLifecycleOwner, Observer { response ->
                handleDefault()
                when (response) {
                    is NetworkResponse.Success<MetricsMap> -> {
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
                        Log.e(TAG, "WeekHistoryFragment: ", response.error)
                        Toast.makeText(
                            requireContext(),
                            "Unknown error: $response",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is NetworkResponse.ApiError -> {
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