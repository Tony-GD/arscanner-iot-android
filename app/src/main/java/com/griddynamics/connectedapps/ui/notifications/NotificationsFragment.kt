package com.griddynamics.connectedapps.ui.notifications

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.griddynamics.connectedapps.MainActivity
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.ui.home.Callback
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        home_logout.setOnClickListener {
            getLogoutDialog {
                (requireActivity() as MainActivity).logout()
            }.show()
        }
    }

    private fun getLogoutDialog(callback: Callback): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(R.string.logout_alert_title)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                callback()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .create()
    }
}
