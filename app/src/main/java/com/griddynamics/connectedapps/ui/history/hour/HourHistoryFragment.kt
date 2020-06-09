package com.griddynamics.connectedapps.ui.history.hour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.settings.hour.HourHistoryItem
import kotlinx.android.synthetic.main.fragmnet_hour_history.*

class HourHistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmnet_hour_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataItems = listOf<HourHistoryItem>(
            HourHistoryItem(
                "PM2.5",
                "Normal range: 1-9",
                7.8f,
                9f
            ),
            HourHistoryItem(
                "Temperature",
                "",
                13f,
                60f
            ),
            HourHistoryItem(
                "CO2",
                "Normal range: < 1000",
                400f,
                1000f
            ),
            HourHistoryItem(
                "Unknown metrics",
                "",
                16.6f,
                1000f
            )
        )
        rv_hour_item_metrics.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_hour_item_metrics.adapter = HourHistoryItemsAdapter(dataItems)
    }
}