package com.griddynamics.connectedapps.ui.settings.devices

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.settings.SettingsDeviceItem
import com.griddynamics.connectedapps.ui.settings.SettingsItemsAdapter
import kotlinx.android.synthetic.main.fragment_details_list.*

private const val TAG: String = "DevicesListFragment"

class DevicesListFragment(
    private val loader: DevicesLoader,
    private val deviceSelectedListener: SettingsItemsAdapter.OnDeviceSelectedListener
) : Fragment() {
    private val items = mutableListOf<SettingsDeviceItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleLoading()
        loader.loadDevices().observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated() called $it")
            items.clear()
            items.addAll(it)
            handleDefault()
        })
        rv_settings_devices.adapter = SettingsItemsAdapter(items, deviceSelectedListener)
        rv_settings_devices.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun handleLoading() {
        pb_settings_devices_loading.visibility = View.VISIBLE
    }

    private fun handleDefault() {
        pb_settings_devices_loading.visibility = View.GONE
        rv_settings_devices.adapter?.notifyDataSetChanged()
    }
}