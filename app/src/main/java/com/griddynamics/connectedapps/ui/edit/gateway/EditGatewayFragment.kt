package com.griddynamics.connectedapps.ui.edit.gateway

import android.content.Context
import android.os.Bundle
import android.os.Handler
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
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEvent
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEventsStream
import com.griddynamics.connectedapps.util.getErrorDialog
import com.griddynamics.connectedapps.util.getSuccessDialog
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EditGatewayFragment : DaggerFragment() {

    private lateinit var viewModel: EditGatewayViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var eventsStream: HomeScreenEventsStream
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
        if (viewModel.gateway == null) {
            viewModel.gateway = EMPTY_GATEWAY
        }
        binding.viewModel = viewModel
        eventsStream.events.observe(viewLifecycleOwner, Observer {
            if (it is HomeScreenEvent.SUCCESS) {
                showSuccess()
            }
            if (it is HomeScreenEvent.ERROR) {
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
                tearDownDialog(this)
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

    private fun tearDownDialog(dialog: AlertDialog) {
        eventsStream.events.postValue(HomeScreenEvent.DEFAULT)
        dialog.dismiss()
        findNavController().popBackStack()
    }

}
