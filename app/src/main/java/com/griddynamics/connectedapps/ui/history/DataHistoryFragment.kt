package com.griddynamics.connectedapps.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.griddynamics.connectedapps.MainActivity
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.ui.history.day.DayHistoryFragment
import com.griddynamics.connectedapps.ui.history.hour.HourHistoryFragment
import com.griddynamics.connectedapps.ui.history.week.WeekHistoryFragment
import com.griddynamics.connectedapps.ui.home.TabAdapter
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_data_history.*
import kotlinx.android.synthetic.main.fragment_settings.*
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
        viewModel = ViewModelProvider(this, viewModelFactory)[HistoryFragmentViewModel::class.java]
        adapter = TabAdapter(this)
        adapter.addFragment(HourHistoryFragment(), "Now")
        adapter.addFragment(DayHistoryFragment(viewModel), "Today")
        adapter.addFragment(WeekHistoryFragment(), "Week")
        vp_history_pager.adapter = adapter
        TabLayoutMediator(tl_history_tabs, vp_history_pager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
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
