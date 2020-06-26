package com.griddynamics.connectedapps.ui.home.edit.gateway

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.databinding.EditGatewayFragmentBinding
import com.griddynamics.connectedapps.model.device.EMPTY_GATEWAY
import com.griddynamics.connectedapps.ui.home.edit.events.EditGatewayScreenEventsStream
import com.griddynamics.connectedapps.ui.common.ScreenEvent
import com.griddynamics.connectedapps.util.getErrorDialog
import com.griddynamics.connectedapps.util.getSuccessDialog
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.edit_gateway_fragment.*
import kotlinx.android.synthetic.main.header_layout.view.*
import javax.inject.Inject

private const val TAG: String = "EditGatewayFragment"

class EditGatewayFragment : DaggerFragment() {

    private lateinit var viewModel: EditGatewayViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var eventsStream: EditGatewayScreenEventsStream
    private lateinit var binding: EditGatewayFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.edit_gateway_fragment, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditGatewayViewModel::class.java)
        arguments?.let {
            val gatewayId = EditGatewayFragmentArgs.fromBundle(it).gateway
            val stringIsAdding = EditGatewayFragmentArgs.fromBundle(it).stringIsAdding
            Log.d(TAG, "onViewCreated: $stringIsAdding")
            viewModel.isAdding.set(stringIsAdding)
            viewModel.gateway = viewModel.getGateway(gatewayId)
            if (!viewModel.isAdding.get()) {
                header_layout.tv_header_title.text = viewModel.name
                header_layout.tv_header_address.visibility = View.GONE
                header_layout.ib_header_back_arrow.setOnClickListener { findNavController().popBackStack() }
            }
        }
        Log.d(TAG, "onViewCreated: 2 ${viewModel.isAdding.get()}")
        if (viewModel.gateway == null) {
            viewModel.gateway = EMPTY_GATEWAY
        }
        binding.viewModel = viewModel
        eventsStream.events.observe(viewLifecycleOwner, Observer {
            if (it is ScreenEvent.LOADING) {
                viewModel.isLoading.set(true)
            }
            if (it is ScreenEvent.DEFAULT) {
                viewModel.isLoading.set(false)
            }
            if (it is ScreenEvent.SUCCESS) {
                showSuccess()
            }
            if (it is ScreenEvent.ERROR) {
                showError()
            }
        })
    }

    private fun showSuccess() {
        getSuccessDialog(
            requireContext(),
            getString(R.string.the_gateway_was_added_now_you_can_track_it)
        ).apply {
            show()
            Handler().postDelayed({
                tearDownDialogAndGoBack(this)
            }, 2_500)
        }
    }

    private fun showError() {
        getErrorDialog(
            requireContext()
        ).apply {
            show()
            Handler().postDelayed({
                tearDownDialog(this)
            }, 2_500)
        }
    }

    private fun tearDownDialogAndGoBack(dialog: AlertDialog) {
        tearDownDialog(dialog)
        findNavController().popBackStack()
    }

    private fun tearDownDialog(dialog: AlertDialog) {
        eventsStream.events.postValue(ScreenEvent.DEFAULT)
        dialog.dismiss()
    }

}
