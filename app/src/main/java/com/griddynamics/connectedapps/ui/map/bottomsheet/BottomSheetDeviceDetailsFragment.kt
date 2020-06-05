package com.griddynamics.connectedapps.ui.map.bottomsheet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.gateway.network.api.MetricsMap
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.ui.edit.device.*
import kotlinx.android.synthetic.main.bottom_dialog_layout.*
import kotlinx.android.synthetic.main.circular_bar_layout.view.*

private const val TAG: String = "BottomSheetDevic"
class BottomSheetDeviceDetailsFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialog_layout, container, false)
    }

    private var metricsMap: MetricsMap? = null
    private var device: DeviceResponse? = null

    fun setDeviceInfo(metricsMap: MetricsMap, device: DeviceResponse) {
        this.metricsMap = metricsMap
        this.device = device
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog?.cl_bottom_sheet_container?.minHeight = 400
        Log.d(TAG, "onStart() called ${metricsMap}")
        metricsMap?.let {
            tv_bottom_title.text = device?.displayName
            it[CO2]?.firstOrNull()?.values?.firstOrNull()?.toSafeFloat()?.let {
                progress_container_1.visibility = View.VISIBLE
                progress_container_1.progress_view.progress = it
                progress_container_1.tv_progress_text.text = it.toString()
                progress_container_0.tv_label.text = CO2
            }
            it[TEMP]?.firstOrNull()?.values?.firstOrNull()?.toSafeFloat()?.let {
                progress_container_2.visibility = View.VISIBLE
                progress_container_2.progress_view.progress = it
                progress_container_2.tv_progress_text.text = it.toString()
                progress_container_0.tv_label.text = TEMP
            }
            it[HUMIDITY]?.firstOrNull()?.values?.firstOrNull()?.toSafeFloat()?.let {
                progress_container_3.visibility = View.VISIBLE
                progress_container_3.progress_view.progress = it
                progress_container_3.tv_progress_text.text = it.toString()
                progress_container_0.tv_label.text = HUMIDITY
            }
            it[PM2_5]?.firstOrNull()?.values?.firstOrNull()?.toSafeFloat()?.let {
                progress_container_0.visibility = View.VISIBLE
                progress_container_0.progress_view.progress = it
                progress_container_0.tv_progress_text.text = it.toString()
                progress_container_0.tv_label.text = PM2_5
            }
            it[PM1_0]?.firstOrNull()?.values?.firstOrNull()?.toSafeFloat()
            it[PM10]?.firstOrNull()?.values?.firstOrNull()?.toSafeFloat()
            it[DEFAULT]?.firstOrNull()?.values?.firstOrNull()?.toSafeFloat()?.let {
                progress_container_0.visibility = View.VISIBLE
                progress_container_1.visibility = View.GONE
                progress_container_2.visibility = View.GONE
                progress_container_3.visibility = View.GONE
                progress_container_0.progress_view.progress = it
                progress_container_0.tv_progress_text.text = it.toString()
                progress_container_0.tv_label.text = DEFAULT
            }

        }
    }

    private fun String?.toSafeFloat(): Float = try {
        "$this".toFloat()
    } catch (e: Exception) {
        0f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val colorList0 = intArrayOf(Color.GREEN, Color.LTGRAY)
        progress_container_0.progress_view.applyGradient(colorList0)
        val colorList1 = intArrayOf(Color.YELLOW, Color.RED)
        progress_container_1.progress_view.applyGradient(colorList1)
        val colorList2 = intArrayOf(Color.DKGRAY, Color.CYAN)
        progress_container_2.progress_view.applyGradient(colorList2)
    }
}