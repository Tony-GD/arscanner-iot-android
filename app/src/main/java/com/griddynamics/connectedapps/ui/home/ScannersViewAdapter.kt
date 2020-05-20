package com.griddynamics.connectedapps.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.gateway.api.ApiSuccessResponse

typealias Callback = () -> Unit

class ScannersViewAdapter(
    private val viewModel: HomeViewModel
) : RecyclerView.Adapter<ScannersViewAdapter.ScannersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannersViewHolder {
        return ScannersViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.air_scanner_item_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return when (val value = viewModel.devices.value) {
            is ApiSuccessResponse -> value.body.devices.size
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: ScannersViewHolder, position: Int) {
        viewModel.devices.value?.let { devicesResponse ->
            when (devicesResponse) {
                is ApiSuccessResponse -> {
                    val device = devicesResponse.body.devices[position]
                    holder.text.text = device.deviceId
                    holder.edit.setOnClickListener {
                        viewModel.onEditDevice(device)
                    }
                    holder.remove.setOnClickListener {
                        createRemoveAlert(it.context) {
                            viewModel.onRemoveDevice(device)
                        }.show()
                    }
                }
            }
        }
    }

    private fun createRemoveAlert(context: Context, callback: Callback): AlertDialog {
        return AlertDialog.Builder(context)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                callback()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .setTitle(R.string.remove_title)
            .setMessage(R.string.remove_device_description)
            .create()
    }

    class ScannersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.device_item_name_tv)
        val edit: ImageView = view.findViewById(R.id.device_item_edit_iv)
        val remove: ImageView = view.findViewById(R.id.device_item_remove_iv)
    }
}

