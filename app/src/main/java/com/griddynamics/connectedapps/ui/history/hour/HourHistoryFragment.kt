package com.griddynamics.connectedapps.ui.history.hour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.settings.hour.HourHistoryItem
import com.griddynamics.connectedapps.ui.history.HistoryViewModel
import kotlinx.android.synthetic.main.fragmnet_hour_history.*

class HourHistoryFragment(private val viewModel: HistoryViewModel) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmnet_hour_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metrics = mutableMapOf<String, HourHistoryItem>()
        rv_hour_item_metrics.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_hour_item_metrics.adapter = HourHistoryItemsAdapter(metrics.values)
        viewModel.subscribeForMetrics("${viewModel.deviceId}")
            .observe(viewLifecycleOwner, Observer { response ->
                metrics[response.metricName] = HourHistoryItem(
                    response.metricName,
                    "Normal range: < 1000",
                    response.value,
                    response.maxValue
                )
                rv_hour_item_metrics.adapter?.notifyDataSetChanged()
            })

    }
}