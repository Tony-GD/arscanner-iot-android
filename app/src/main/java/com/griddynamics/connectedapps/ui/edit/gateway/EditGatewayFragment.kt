package com.griddynamics.connectedapps.ui.edit.gateway

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.device.EMPTY_GATEWAY
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.edit_gateway_fragment.*
import javax.inject.Inject

class EditGatewayFragment : DaggerFragment() {

    private lateinit var viewModel: EditGatewayViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_gateway_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditGatewayViewModel::class.java)
        if (viewModel.gateway == null) {
            viewModel.gateway = EMPTY_GATEWAY
        }
        et_gateway_name.addTextChangedListener {text->
            viewModel.gateway?.apply {
                if (text?.isNotBlank() == true) {
                    displayName = text.toString()
                }
            }
        }
        et_gateway_key.addTextChangedListener { text->
            viewModel.gateway?.apply {
                if (text?.isNotBlank() == true) {
                    key = text.toString()
                }
            }
        }
        btn_edit_gateway_save.setOnClickListener {
            viewModel.saveGateway()
        }
    }

}
