package com.griddynamics.connectedapps.ui.gateway

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.gateway_details_fragment.*
import kotlinx.android.synthetic.main.header_layout.view.*
import javax.inject.Inject

class GatewayDetailsFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[GatewayDetailsViewModel::class.java]
        arguments?.let {
            val gatewayId = GatewayDetailsFragmentArgs.fromBundle(it).gatewayId
            val gateway = viewModel.getGateway(gatewayId)
            val devices = viewModel.getGatewayDevices(gatewayId)
            gateway_details_header.tv_header_title.text = gateway?.displayName
            rv_gateway_details_devices_container.layoutManager = LinearLayoutManager(context)
            rv_gateway_details_devices_container.adapter =
                GatewayDetailsDevicesAdapter(viewLifecycleOwner, devices, viewModel)
        }
    }
}