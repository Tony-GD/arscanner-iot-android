package com.griddynamics.connectedapps.ui.history.hour

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.di.ui.cutom.ColorAdaptedProgressView
import com.griddynamics.connectedapps.model.settings.hour.HourHistoryItem

class HourHistoryItemsAdapter(
    private val dataItems: List<HourHistoryItem>
) :
    RecyclerView.Adapter<HourHistoryItemsAdapter.HourHistoryViewHolder>() {
    class HourHistoryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun setItem(item: HourHistoryItem) {
            view.findViewById<TextView>(R.id.tv_day_history_item_title).text = item.title
            view.findViewById<TextView>(R.id.tv_day_history_item_range).text = item.rangeDescription
            view.findViewById<TextView>(R.id.tv_progress_text).text = item.value.toString()
            val progressView = view.findViewById<ColorAdaptedProgressView>(R.id.progress_view)
            progressView.maxValue = item.maxValue
            progressView.progress = item.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourHistoryViewHolder {
        return HourHistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.hour_history_item_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataItems.size
    }

    override fun onBindViewHolder(holder: HourHistoryViewHolder, position: Int) {
        holder.setItem(dataItems[position])
    }
}