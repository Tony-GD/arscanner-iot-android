package com.griddynamics.connectedapps.ui.map.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.databinding.BottomDialogItemLayoutBinding
import com.griddynamics.connectedapps.model.DefaultScannersResponse

class BottomSheetDialogMetricsAdapter(private val entries: Collection<DefaultScannersResponse>) :
    RecyclerView.Adapter<BottomSheetDialogMetricsAdapter.MetricViewHolder>() {

    class MetricViewHolder(val binding: BottomDialogItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetDialogMetricsAdapter.MetricViewHolder {
        return MetricViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.bottom_dialog_item_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onBindViewHolder(
        holder: BottomSheetDialogMetricsAdapter.MetricViewHolder,
        position: Int
    ) {
        holder.binding.response = entries.toTypedArray()[position]
    }
}