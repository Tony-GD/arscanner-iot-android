package com.griddynamics.connectedapps.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.databinding.FragmentHomeBindingImpl
import com.griddynamics.connectedapps.repository.local.LocalStorage
import com.griddynamics.connectedapps.model.device.DeviceRequest
import com.griddynamics.connectedapps.ui.edit.device.EditDeviceFragment
import com.griddynamics.connectedapps.ui.edit.gateway.EditGatewayFragment
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


private const val TAG: String = "HomeFragment"

class HomeFragment : DaggerFragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var bindingImpl: FragmentHomeBindingImpl

    @Inject
    lateinit var localStorage: LocalStorage

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var adapter: TabAdapter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private fun navigateToEditFragment(data: DeviceRequest) {
        val actionGlobalNavigationEdit = HomeFragmentDirections.ActionNavigationHomeToNavigationEdit()
        actionGlobalNavigationEdit.setDevice(data.deviceId)
        findNavController().navigate(actionGlobalNavigationEdit)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingImpl =
            DataBindingUtil.inflate(
                layoutInflater, R.layout.fragment_home, container, false
            )
        homeViewModel =
            ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        bindingImpl.lifecycleOwner = viewLifecycleOwner
        bindingImpl.viewModel = homeViewModel

        return bindingImpl.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter =  TabAdapter(this)
        adapter.addFragment(EditDeviceFragment(), "Device")
        adapter.addFragment(EditGatewayFragment(), "Gateway")
        vp_home_pager.adapter = adapter
        TabLayoutMediator(tl_home_tabs, vp_home_pager) {tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

}
