package com.griddynamics.connectedapps.ui.map.bottomsheet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.ui.map.MapFragment
import kotlinx.android.synthetic.main.bottom_dialog_layout.*

private const val TAG: String = "BottomSheetDevic"

class BottomSheetDeviceDetailsFragment(
    private val onDeviceSelectedListener: MapFragment.OnDeviceSelectedListener
) :
    BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
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
            BottomSheetDialogMetricsAdapter(viewModel.metricsMap.values) {
                device?.let { onDeviceSelectedListener.onDeviceSelected(it) }
                dismiss()
            }
        dialog?.cl_bottom_sheet_container?.minHeight = 400
        Log.d(TAG, "onStart() called device [${viewModel.device}]")
        device?.let {
            tv_bottom_title.text = it.displayName
            viewModel.subscribeForMetrics(it).observe(viewLifecycleOwner, Observer { response ->
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