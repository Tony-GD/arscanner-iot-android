package com.griddynamics.connectedapps.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.settings.SettingsDeviceItem


class SettingsItemsAdapter(private val scannerItems: List<SettingsDeviceItem>) :
    RecyclerView.Adapter<SettingsItemsAdapter.SettingsItemViewHolder>() {

    class SettingsItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        var name: String
            get() = view.findViewById<TextView>(R.id.tv_settings_item_name).text.toString()
            set(value) {
                view.findViewById<TextView>(R.id.tv_settings_item_name).text = value
            }

        var address: String
            get() = view.findViewById<TextView>(R.id.tv_settings_item_address).text.toString()
            set(value) {
                view.findViewById<TextView>(R.id.tv_settings_item_address).text = value
            }
    }

    override fun getItemViewType(position: Int): Int {
        return scannerItems[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsItemViewHolder {
        return when (viewType) {
            SettingsDeviceItem.TYPE_DEVICE -> SettingsItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.settings_device_item_layout, parent, false)
            )
            else -> SettingsItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.settings_gateway_item_layout, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return scannerItems.size
    }

    override fun onBindViewHolder(holder: SettingsItemViewHolder, position: Int) {
        val item = scannerItems[position]
        holder.name = item.displayName
        holder.address = item.address
    }
}