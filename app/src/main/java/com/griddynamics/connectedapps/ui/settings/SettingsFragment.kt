package com.griddynamics.connectedapps.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.tabs.TabLayoutMediator
import com.griddynamics.connectedapps.MainActivity
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.repository.stream.GatewayStream
import com.griddynamics.connectedapps.ui.home.Callback
import com.griddynamics.connectedapps.ui.home.TabAdapter
import com.griddynamics.connectedapps.ui.settings.devices.DevicesListFragment
import com.griddynamics.connectedapps.ui.settings.devices.DevicesLoader
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.user_info_layout.view.*
import javax.inject.Inject
private const val TAG: String = "SettingsFragment"

class SettingsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var gatewayStream: GatewayStream
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var adapter: TabAdapter

    private var onDeviceSelectedListener = object : SettingsItemsAdapter.OnDeviceSelectedListener {
        override fun onDeviceSelected(deviceId: String, address: String) {
            Log.d(TAG, "onDeviceSelected() called with: deviceId = [$deviceId], address = [$address]")
            navigateToChartFragment(deviceId, address)
        }

        override fun onGatewaySelected(gatewayId: String) {
            Log.d(TAG, "onGatewaySelected() called with: gatewayId = [$gatewayId]")
            navigateToGatewayDetailsFragment(gatewayId)
        }

    }

    private fun navigateToChartFragment(deviceId: String, address: String) {
        val actionGlobalNavigationHistory =
            SettingsFragmentDirections.ActionNavigationSettingsToNavigationHistory()
        actionGlobalNavigationHistory.setAddress(address)
        actionGlobalNavigationHistory.setDevice(deviceId)
        findNavController().navigate(actionGlobalNavigationHistory)
    }

    private fun navigateToGatewayDetailsFragment(gatewayId: String) {
        val action = SettingsFragmentDirections.ActionNavigationSettingsToGatewayDetailsFragment()
        action.setGatewayId(gatewayId)
        findNavController().navigate(action)
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
            requireContext().getText(R.string.profile)
        settings_user_layout.tv_settings_email.text = user.email
        settings_user_layout.tv_settings_username.text = "${user.givenName} ${user.familyName}"
        home_logout.setOnClickListener {
            getLogoutDialog {
                (requireActivity() as MainActivity).logout()
            }.show()
        }
        btn_settings_add.setOnClickListener { navigateToHomeFragment() }
        ib_header_back_arrow.setOnClickListener { activity?.onBackPressed() }
        adapter = TabAdapter(this)
        adapter.addFragment(
            DevicesListFragment(
                DevicesLoader.UserDevicesLoader(settingsViewModel),
                onDeviceSelectedListener
            ), "My devices"
        )
        adapter.addFragment(
            DevicesListFragment(
                DevicesLoader.PublicDevicesLoader(settingsViewModel),
                onDeviceSelectedListener
            ), "Public devices"
        )
        vp_settings_pager.adapter = adapter
        TabLayoutMediator(tl_settings_tabs, vp_settings_pager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

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
