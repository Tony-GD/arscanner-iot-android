package com.griddynamics.connectedapps.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.griddynamics.connectedapps.R
import kotlinx.android.synthetic.main.alert_error_layout.view.*
import kotlinx.android.synthetic.main.alert_success_layout.view.*

fun getSuccessDialog(context: Context, message: String): AlertDialog {
    return AlertDialog.Builder(context)
        .setView(
            LayoutInflater
                .from(context)
                .inflate(R.layout.alert_success_layout, null)
                .apply {
                    tv_success_message.text = message
                }
        )
        .create()
        .apply {
            this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
}


fun getErrorDialog(context: Context, message: String? = null): AlertDialog {
    return AlertDialog.Builder(context)
        .setView(
            LayoutInflater
                .from(context)
                .inflate(R.layout.alert_error_layout, null)
                .apply {
                    message?.let {
                        tv_error_dialog_message.text = it
                    }
                }
        )
        .create()
        .apply {
            this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
}