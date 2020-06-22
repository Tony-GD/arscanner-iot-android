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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.griddynamics.connectedapps.MainActivity
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.repository.network.api.NetworkResponse
import com.griddynamics.connectedapps.ui.history.day.DayHistoryFragment
import com.griddynamics.connectedapps.ui.history.day.events.DayHistoryEventsStream
import com.griddynamics.connectedapps.ui.history.hour.HourHistoryFragment
import com.griddynamics.connectedapps.ui.history.hour.events.HourHistoryEventsStream
import com.griddynamics.connectedapps.ui.history.week.WeekHistoryFragment
import com.griddynamics.connectedapps.ui.history.week.events.WeekHistoryEventsStream
import com.griddynamics.connectedapps.ui.home.Callback
import com.griddynamics.connectedapps.ui.home.TabAdapter
import com.griddynamics.connectedapps.util.getErrorDialog
import com.griddynamics.connectedapps.util.getSuccessDialog
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_data_history.*
import kotlinx.android.synthetic.main.header_layout.view.*
import javax.inject.Inject

private const val TAG: String = "HistoryFragment"

class HistoryFragment : DaggerFragment() {
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: TabAdapter
    private var address: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dayHistoryEventsStream: DayHistoryEventsStream

    @Inject
    lateinit var hourHistoryEventsStream: HourHistoryEventsStream

    @Inject
    lateinit var weekHistoryEventsStream: WeekHistoryEventsStream

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
        viewModel = ViewModelProvider(this, viewModelFactory)[HistoryViewModel::class.java]
        arguments?.let {
            viewModel.deviceId = HistoryFragmentArgs.fromBundle(it).device
            address = HistoryFragmentArgs.fromBundle(it).address
        }
        history_header.tv_header_comment.setOnClickListener { navigateToEdit() }
        history_header.ib_header_back_arrow.setOnClickListener { requireActivity().onBackPressed() }
        history_header.tv_header_title.text = getString(R.string.history)
        history_header.ll_header_extra_container.visibility = View.VISIBLE
        history_header.tv_header_address.text = address
        btn_add_to_widget.setOnClickListener {
            viewModel.addToWidget()
            Snackbar.make(history_root, getString(R.string.added_to_the_widget), Snackbar.LENGTH_LONG).show()
        }
        btn_history_remove.setOnClickListener {
            getRemoveAlertDialog {
                viewModel.removeDevice("${viewModel.deviceId}")
                    .observe(viewLifecycleOwner, Observer {
                        when (it) {
                            is NetworkResponse.Success<*> -> {
                                getSuccessDialog(
                                    requireContext(),
                                    getString(R.string.device_was_removed)
                                ).apply {
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
                                getErrorDialog(requireContext()).apply {
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
        adapter = TabAdapter(this)
        adapter.addFragment(
            HourHistoryFragment(viewModel, hourHistoryEventsStream),
            getString(R.string.now)
        )
        adapter.addFragment(
            DayHistoryFragment(viewModel, dayHistoryEventsStream),
            getString(R.string.today)
        )
        adapter.addFragment(
            WeekHistoryFragment(viewModel, weekHistoryEventsStream),
            getString(R.string.week)
        )
        vp_history_pager.adapter = adapter
        TabLayoutMediator(tl_history_tabs, vp_history_pager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

    private fun navigateToEdit() {
        val action = HistoryFragmentDirections.ActionNavigationHistoryToNavigationEdit()
        action.setDevice("${viewModel.deviceId}")
        action.setStringIsAdding(false)
        findNavController().navigate(action)
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


    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).hideTabBar()
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as MainActivity).showTabBar()
    }


}
