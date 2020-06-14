package com.griddynamics.connectedapps.ui.map.bottomsheet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.device.DeviceResponse
import kotlinx.android.synthetic.main.bottom_dialog_layout.*

private const val TAG: String = "BottomSheetDevic"

class BottomSheetDeviceDetailsFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialog_layout, container, false)
    }

    private lateinit var viewModel: BottomSheetDialogViewModel
    private var device: DeviceResponse? = null

    fun setDeviceInfo(device: DeviceResponse) {
        this.device = device
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(this)[BottomSheetDialogViewModel::class.java]
        rv_bottom_metrics_container.layoutManager = GridLayoutManager(requireContext(), 4)
        rv_bottom_metrics_container.adapter =
            BottomSheetDialogMetricsAdapter(viewModel.metricsMap.values)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog?.cl_bottom_sheet_container?.minHeight = 400
        Log.d(TAG, "onStart() called device [${viewModel.device}]")
        device?.let {
            tv_bottom_title.text = it.displayName
            viewModel.subscribeForMetrics(it).observe(viewLifecycleOwner, Observer { response ->
                Log.d(TAG, "onStart() called with: response = [$response]")
                viewModel.metricsMap[response.metricName] = response
                rv_bottom_metrics_container.adapter?.notifyDataSetChanged()
            })
        }
    }

    private fun String?.toSafeFloat(): Float = try {
        "$this".toFloat()
    } catch (e: Exception) {
        0f
    }

}