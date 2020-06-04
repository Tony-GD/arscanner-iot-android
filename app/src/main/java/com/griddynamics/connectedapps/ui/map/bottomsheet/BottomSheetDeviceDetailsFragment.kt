package com.griddynamics.connectedapps.ui.map.bottomsheet

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.griddynamics.connectedapps.R
import kotlinx.android.synthetic.main.bottom_dialog_layout.*

class BottomSheetDeviceDetailsFragment: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialog_layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.cl_bottom_sheet_container?.minHeight = 400
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val colorList0 = intArrayOf(Color.GREEN,Color.LTGRAY)
        progressView0.applyGradient(colorList0)
        val colorList1 = intArrayOf(Color.YELLOW, Color.RED)
        progressView1.applyGradient(colorList1)
        val colorList2 = intArrayOf(Color.DKGRAY, Color.CYAN)
        progressView2.applyGradient(colorList2)
    }
}