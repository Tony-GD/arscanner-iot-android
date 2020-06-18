package com.griddynamics.connectedapps.ui.gateway.device

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.databinding.GatewayDetailsDeviceItemMetricLayoutBinding
import com.griddynamics.connectedapps.model.DefaultScannersResponse

class DeviceMetricsAdapter(private val metrics: Collection<DefaultScannersResponse>) :
    RecyclerView.Adapter<DeviceMetricsAdapter.DeviceMetricViewHolder>() {
    class DeviceMetricViewHolder(val binding: GatewayDetailsDeviceItemMetricLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceMetricViewHolder {
        return DeviceMetricViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.gateway_details_device_item_metric_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return metrics.size
    }

    override fun onBindViewHolder(holder: DeviceMetricViewHolder, position: Int) {
        holder.binding.item = metrics.toTypedArray()[position]
    }
}