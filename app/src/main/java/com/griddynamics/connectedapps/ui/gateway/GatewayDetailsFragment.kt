package com.griddynamics.connectedapps.ui.gateway

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.ui.gateway.events.GatewayDetailsEventsStream
import com.griddynamics.connectedapps.ui.gateway.events.GatewayDetailsScreenEvent
import com.griddynamics.connectedapps.ui.home.Callback
import com.griddynamics.connectedapps.util.getErrorDialog
import com.griddynamics.connectedapps.util.getSuccessDialog
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.gateway_details_fragment.*
import kotlinx.android.synthetic.main.header_layout.view.*
import javax.inject.Inject

private const val TAG: String = "GatewayDetailsFragment"

class GatewayDetailsFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var eventsStream: GatewayDetailsEventsStream
    private lateinit var viewModel: GatewayDetailsViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.gateway_details_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadDevices()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[GatewayDetailsViewModel::class.java]
        gateway_details_header.ib_header_back_arrow.setOnClickListener { findNavController().popBackStack() }
        arguments?.let {
            val gatewayId = GatewayDetailsFragmentArgs.fromBundle(it).gatewayId
            viewModel.gateway = viewModel.getGateway(gatewayId)
            val devices = viewModel.getGatewayDevices(gatewayId)
            gateway_details_header.tv_header_title.text = viewModel.gateway?.displayName
            rv_gateway_details_devices_container.layoutManager = LinearLayoutManager(context)
            rv_gateway_details_devices_container.adapter =
                GatewayDetailsDevicesAdapter(viewLifecycleOwner, devices, viewModel)
        }
        btn_gateway_add_device.setOnClickListener { navigateToAddDeviceScreen() }
        btn_gateway_delete.setOnClickListener {
            getRemoveAlertDialog {
                handleLoading()
                viewModel.deleteGateway()
            }.show()
        }
        eventsStream.event.observe(viewLifecycleOwner, Observer {
            if (it is GatewayDetailsScreenEvent.NavigateToDeviceDetailsScreenEvent) {
                navigateToDeviceHistoryScreen(it.deviceId, it.address)
                eventsStream.event.value = GatewayDetailsScreenEvent.DEFAULT_STATE_EVENT
            }
            if (it is GatewayDetailsScreenEvent.SUCCESS) {
                getSuccessDialog(requireContext(), getString(R.string.gateway_was_removed)).apply {
                    show()
                    Handler().postDelayed({
                        tearDownDialog(this)
                        findNavController().popBackStack()
                    }, 2_500)
                }
            }
            if (it is GatewayDetailsScreenEvent.Error) {
                getErrorDialog(requireContext(), it.message).apply {
                    show()
                    Handler().postDelayed({
                        tearDownDialog(this)
                    }, 2_500)
                }
            }
        })
    }

    private fun getRemoveAlertDialog(action: Callback): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.remove_gateway_title))
            .setMessage(getString(R.string.remove_gateway_prompt))
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
                action()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .create()
    }

    private fun handleLoading() {
        pb_gateway_details_loading.visibility = View.VISIBLE
        btn_gateway_delete.isEnabled = false
    }

    private fun handleDefault() {
        pb_gateway_details_loading.visibility = View.GONE
        btn_gateway_delete.isEnabled = true
    }

    private fun tearDownDialog(alertDialog: AlertDialog) {
        eventsStream.event.value = GatewayDetailsScreenEvent.DEFAULT_STATE_EVENT
        alertDialog.dismiss()
        handleDefault()
    }

    private fun navigateToAddDeviceScreen() {
        val action = GatewayDetailsFragmentDirections.ActionGatewayDetailsFragmentToNavigationHome()
        findNavController().navigate(action)
    }

    private fun navigateToDeviceHistoryScreen(device: String, address: String) {
        val action =
            GatewayDetailsFragmentDirections.ActionGatewayDetailsFragmentToNavigationHistory()
        action.setDevice(device)
        action.setAddress(address)
        findNavController().navigate(action)
    }
}