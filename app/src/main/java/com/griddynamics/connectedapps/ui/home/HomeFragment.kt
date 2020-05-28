package com.griddynamics.connectedapps.ui.home

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.griddynamics.connectedapps.MainActivity
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.databinding.FragmentHomeBindingImpl
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.model.device.DeviceRequest
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
        homeViewModel.onEditListener = { navigateToEditFragment(it) }
        bindingImpl.lifecycleOwner = viewLifecycleOwner
        bindingImpl.viewModel = homeViewModel

        return bindingImpl.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
