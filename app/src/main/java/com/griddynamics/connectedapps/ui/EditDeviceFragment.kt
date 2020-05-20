package com.griddynamics.connectedapps.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.databinding.EditDeviceFragmentBinding
import com.griddynamics.connectedapps.model.EMPTY_DEVICE_REQUEST

class EditDeviceFragment : Fragment() {

    companion object {
        private const val TAG: String = "EditDeviceFragment"
        fun newInstance() = EditDeviceFragment()
    }


    private lateinit var binding: EditDeviceFragmentBinding
    private lateinit var viewModel: EditDeviceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_device_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated() called with: view = [$view], savedInstanceState = [$savedInstanceState]")
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditDeviceViewModel::class.java)
        binding.viewModel = viewModel
        arguments?.apply {
            val device = EditDeviceFragmentArgs.fromBundle(arguments).device
            viewModel.device = EMPTY_DEVICE_REQUEST.copy(deviceId = device)
        }
    }

}
