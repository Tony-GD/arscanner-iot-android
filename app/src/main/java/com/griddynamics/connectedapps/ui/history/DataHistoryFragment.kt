package com.griddynamics.connectedapps.ui.history

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.griddynamics.connectedapps.MainActivity
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.gateway.network.api.NetworkResponse
import com.griddynamics.connectedapps.ui.history.day.DayHistoryFragment
import com.griddynamics.connectedapps.ui.history.hour.HourHistoryFragment
import com.griddynamics.connectedapps.ui.history.week.WeekHistoryFragment
import com.griddynamics.connectedapps.ui.home.Callback
import com.griddynamics.connectedapps.ui.home.TabAdapter
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.alert_success_layout.view.*
import kotlinx.android.synthetic.main.fragment_data_history.*
import kotlinx.android.synthetic.main.header_layout.view.*
import javax.inject.Inject

private const val TAG: String = "DataHistoryFragment"

class DataHistoryFragment : DaggerFragment() {
    private lateinit var viewModel: HistoryFragmentViewModel
    private lateinit var adapter: TabAdapter
    private var address: String? = null
    private var deviceId: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_data_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            deviceId = DataHistoryFragmentArgs.fromBundle(it).device
            address = DataHistoryFragmentArgs.fromBundle(it).address
        }
        history_header.ib_header_back_arrow.setOnClickListener { requireActivity().onBackPressed() }
        history_header.tv_header_title.text = getString(R.string.history)
        history_header.ll_header_extra_container.visibility = View.VISIBLE
        history_header.tv_header_address.text = address
        btn_history_remove.setOnClickListener {
            getRemoveAlertDialog {
                viewModel.removeDevice("$deviceId")
                    .observe(viewLifecycleOwner, Observer {
                        when (it) {
                            is NetworkResponse.Success<*> -> {
                                getSuccessDialog(getString(R.string.device_was_removed)).apply {
                                    show()
                                    Handler().postDelayed({
                                        requireActivity().runOnUiThread {
                                            dismiss()
                                            findNavController().popBackStack()
                                        }
                                    }, 2500)
                                }
                            }
                            is NetworkResponse.NetworkError -> {
                                Log.e(TAG, "DataHistoryFragment: network error", it.error)
                            }
                            else -> {
                                Log.e(TAG, "DataHistoryFragment: error $it")
                                getErrorDialog().apply {
                                    show()
                                    Handler().postDelayed({
                                        dismiss()
                                    }, 2500)
                                }
                            }
                        }
                    })
            }.show()
        }
        viewModel = ViewModelProvider(this, viewModelFactory)[HistoryFragmentViewModel::class.java]
        adapter = TabAdapter(this)
        adapter.addFragment(HourHistoryFragment(), getString(R.string.now))
        adapter.addFragment(DayHistoryFragment(viewModel), getString(R.string.today))
        adapter.addFragment(WeekHistoryFragment(), getString(R.string.week))
        vp_history_pager.adapter = adapter
        TabLayoutMediator(tl_history_tabs, vp_history_pager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

    private fun getRemoveAlertDialog(action: Callback): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.remove_device_title))
            .setMessage(getString(R.string.remove_device_prompt))
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
                action()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .create()
    }

    private fun getErrorDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setView(
                LayoutInflater
                    .from(requireContext())
                    .inflate(R.layout.alert_error_layout, null)
            )
            .create()
    }

    private fun getSuccessDialog(message: String): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setView(
                LayoutInflater
                    .from(requireContext())
                    .inflate(R.layout.alert_success_layout, null)
                    .apply {
                        tv_success_message.text = message
                    }
            )
            .create()
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).hideTabBar()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).showTabBar()
    }


}
