package com.griddynamics.connectedapps.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.databinding.FragmentHomeBindingImpl
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject


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
            ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]
        bindingImpl.lifecycleOwner = viewLifecycleOwner
        bindingImpl.viewModel = homeViewModel
        return bindingImpl.root
    }
}
