package com.griddynamics.connectedapps.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.griddynamics.connectedapps.MainActivity
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.settings.SettingsDeviceItem
import com.griddynamics.connectedapps.ui.home.Callback
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.user_info_layout.view.*
import javax.inject.Inject

class SettingsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var settingsViewModel: SettingsViewModel
    private var onDeviceSelectedListener = object : SettingsItemsAdapter.OnDeviceSelectedListener {
        override fun onDeviceSelected(deviceId: String) {
            navigateToChartFragment(deviceId)
        }

        override fun onGatewaySelected(gatewayId: String) {
//            TODO("Not yet implemented")
        }

    }

    private fun navigateToChartFragment(deviceId: String) {
        val actionGlobalNavigationHistory =
            SettingsFragmentDirections.ActionNavigationSettingsToNavigationHistory()
        actionGlobalNavigationHistory.setDevice(deviceId)
        findNavController().navigate(actionGlobalNavigationHistory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        settingsViewModel =
            ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = settingsViewModel.user
        Glide.with(this)
            .load(user.photoUrl)
            .transform(CircleCrop())
            .into(settings_user_layout.iv_settings_user)
        settings_header.findViewById<TextView>(R.id.tv_header_title).text =
            requireContext().getText(R.string.settings)
        settings_user_layout.tv_settings_email.text = user.email
        settings_user_layout.tv_settings_username.text = "${user.givenName} ${user.familyName}"
        home_logout.setOnClickListener {
            getLogoutDialog {
                (requireActivity() as MainActivity).logout()
            }.show()
        }
        btn_settings_add.setOnClickListener { navigateToHomeFragment() }
        ib_header_back_arrow.setOnClickListener { activity?.onBackPressed() }
        val items = mutableListOf<SettingsDeviceItem>()
        rv_settings_devices.adapter = SettingsItemsAdapter(items, onDeviceSelectedListener)
        rv_settings_devices.layoutManager = LinearLayoutManager(requireContext())
        settingsViewModel.loadUserGateways().observe(viewLifecycleOwner, Observer { gateways ->
            gateways.forEach {
                items.add(
                    SettingsDeviceItem(
                        "${it.gatewayId}",
                        SettingsDeviceItem.TYPE_GATEWAY,
                        "${it.displayName}",
                        ""
                    )
                )
            }
            (rv_settings_devices.adapter as SettingsItemsAdapter).notifyDataSetChanged()
        })
        settingsViewModel.loadUserDevices().observe(viewLifecycleOwner, Observer { devices ->
            devices.forEach {
                val item = SettingsDeviceItem(
                    "${it.deviceId}",
                    SettingsDeviceItem.TYPE_DEVICE,
                    "${it.displayName}",
                    ""
                )
                settingsViewModel.loadAddress(it.location)
                    .observe(viewLifecycleOwner, Observer { address ->
                        item.address = address
                        (rv_settings_devices.adapter as SettingsItemsAdapter).notifyDataSetChanged()
                    })
                items.add(item)
            }
        })
    }

    private fun navigateToHomeFragment() {
        val actionGlobalNavigationHome =
            SettingsFragmentDirections.ActionNavigationSettingsToNavigationHome()
        findNavController().navigate(actionGlobalNavigationHome)
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
