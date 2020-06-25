package com.griddynamics.connectedapps.ui.gateway

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.databinding.GatewayDetailsDeviceItemLayoutBinding
import com.griddynamics.connectedapps.model.DefaultScannersResponse
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.ui.gateway.device.DeviceMetricsAdapter
import kotlinx.android.synthetic.main.gateway_details_device_item_description.view.*

class GatewayDetailsDevicesAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val devices: List<DeviceResponse>,
    private val viewModel: GatewayDetailsViewModel
) :
    RecyclerView.Adapter<GatewayDetailsDevicesAdapter.GatewayDetailsDeviceViewHolder>() {

    class GatewayDetailsDeviceViewHolder(
        private val binding: GatewayDetailsDeviceItemLayoutBinding,
        private val lifecycleOwner: LifecycleOwner,
        private val viewModel: GatewayDetailsViewModel
    ) :
        RecyclerView.ViewHolder(binding.root) {
        var address: String = ""
        fun setDevice(device: DeviceResponse) {
            val metrics = mutableMapOf<String, DefaultScannersResponse>()
            binding.rvGatewayDetailsItemsContainer.layoutManager =
                LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
            binding.rvGatewayDetailsItemsContainer.adapter = DeviceMetricsAdapter(metrics.values)
            if (device.locationDescription != null) {
                address = "${device.locationDescription}"
                binding.deviceDescriptionContainer.tv_description_address.text = device.locationDescription
            } else {
                viewModel.getAddress(device.location).observe(lifecycleOwner, Observer {
                    address = it
                    binding.deviceDescriptionContainer.tv_description_address.text = it
                })
            }
            viewModel.subscribeForMetrics("${device.deviceId}").observe(lifecycleOwner, Observer {
                if (metrics.isEmpty()) {
                    binding.metric = it
                }
                metrics[it.metricName] = it
                binding.rvGatewayDetailsItemsContainer.adapter?.notifyDataSetChanged()
            })
            binding.device = device
            binding.root.setOnClickListener {
                viewModel.onOpenDeviceDetails("${device.deviceId}", address)
            }
            binding.deviceDescriptionContainer.apply {
                tv_description_title.text = device.displayName
                tv_description_address.text = address
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GatewayDetailsDeviceViewHolder {
        return GatewayDetailsDeviceViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.gateway_details_device_item_layout, parent, false
            ),
            lifecycleOwner,
            viewModel
        )
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onBindViewHolder(holder: GatewayDetailsDeviceViewHolder, position: Int) {
        holder.setDevice(devices[position])
    }
}