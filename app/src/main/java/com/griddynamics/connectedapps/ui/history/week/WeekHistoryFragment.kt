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
import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import com.griddynamics.connectedapps.gateway.network.api.NetworkResponse
import com.griddynamics.connectedapps.model.metrics.MetricsResponse
import com.griddynamics.connectedapps.ui.history.HistoryFragmentViewModel
import kotlinx.android.synthetic.main.fragment_week_history.*

private const val TAG: String = "WeekHistoryFragment"

class WeekHistoryFragment(private val viewModel: HistoryFragmentViewModel) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_week_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metrics = listOf<MetricsResponse>(
            MetricsResponse(listOf(), listOf(), listOf()),
            MetricsResponse(listOf(), listOf(), listOf()),
            MetricsResponse(listOf(), listOf(), listOf()),
            MetricsResponse(listOf(), listOf(), listOf()),
            MetricsResponse(listOf(), listOf(), listOf())
        )
        viewModel.getMetrics("${viewModel.deviceId}")
            .observe(viewLifecycleOwner, Observer {
                when (it) {
                    is NetworkResponse.Success<MetricsMap> -> {
                        Log.d(TAG, "onViewCreated: result ${it.body}")
                    }
                    is NetworkResponse.UnknownError -> {
                        Log.e(TAG, "WeekHistoryFragment: ", it.error)
                    }
                    else -> {
                        Log.e(TAG, "WeekHistoryFragment: $it")
                    }
                }
            })
        rv_week_history_charts.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_week_history_charts.adapter = WeekHistoryItemsAdapter(metrics)
    }
}