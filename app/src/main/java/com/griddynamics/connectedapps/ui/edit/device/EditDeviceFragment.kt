package com.griddynamics.connectedapps.ui.edit.device

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.griddynamics.connectedapps.MainActivity
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.databinding.EditDeviceFragmentBinding
import com.griddynamics.connectedapps.model.device.*
import com.griddynamics.connectedapps.model.metrics.JsonMetricViewState
import com.griddynamics.connectedapps.repository.network.api.NetworkResponse
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEvent
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEventsStream
import com.griddynamics.connectedapps.util.getErrorDialog
import com.griddynamics.connectedapps.util.getMapColorFilter
import com.griddynamics.connectedapps.util.getSuccessDialog
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.edit_device_fragment.*
import kotlinx.android.synthetic.main.header_layout.view.*
import kotlinx.android.synthetic.main.location_picker_layout.view.*
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject

private const val TAG: String = "EditDeviceFragment"

class EditDeviceFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var eventsStream: HomeScreenEventsStream
    private lateinit var binding: EditDeviceFragmentBinding
    private lateinit var viewModel: EditDeviceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_device_fragment, container, false)
        return binding.root
    }

    private fun showLocationPicker() {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.location_picker_layout, null)


        val eventsReceiver = object : MapEventsReceiver {
            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }

            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                viewModel.selectedLat = p.latitude.toFloat()
                viewModel.selectedLong = p.longitude.toFloat()
                viewModel.setLocationDescription(p.latitude, p.longitude)
                    .observe(viewLifecycleOwner, Observer {
                        binding.invalidateAll()
                    })
                view.map_picker.overlays.clear()
                val elementMarker = Marker(view.map_picker)
                elementMarker.icon = requireContext().getDrawable(R.drawable.ic_pin)
                elementMarker.position = p
                elementMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                view.map_picker.overlays.add(elementMarker)
                return false
            }
        }
        view.map_picker.overlayManager.tilesOverlay
            .setColorFilter(
                getMapColorFilter(
                    requireContext().getColor(R.color.colorMapBlack)
                )
            )
        val mapEventsOverlay = MapEventsOverlay(eventsReceiver)
        setupMap(view.map_picker)
        view.map_picker.overlays.add(mapEventsOverlay)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
        view.picker_ok_btn.setOnClickListener {
            binding.lat.setText(viewModel.selectedLat.toString())
            binding.lon.setText(viewModel.selectedLong.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setupMap(map: MapView) {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setTileSource(TileSourceFactory.OpenTopo)
        val mapController = map.controller as MapController
        map.setBuiltInZoomControls(false)
        mapController.zoomTo(10)
        map.setUseDataConnection(true)
        map.setMultiTouchControls(true)
        val startPoint = GeoPoint(DEFAULT_LAT.toDouble(), DEFAULT_LONG.toDouble())
        mapController.animateTo(startPoint)
    }

    private fun setupExtraInfo() {
        if (!viewModel.isAdding.get()) {
            cl_edit_extra_info.visibility = View.VISIBLE
            val clientId =
                String.format(getString(R.string.client_id_value), viewModel.device?.gatewayId)
            tv_extra_client_value.text =
                clientId
            val topic = String.format(getString(R.string.topic_value), viewModel.device?.deviceId)
            tv_extra_topic_value.text =
                topic
            ib_extra_client_copy.setOnClickListener {
                setToClipboard(clientId)
            }
            ib_extra_topic_copy.setOnClickListener {
                setToClipboard(topic)
            }
        } else {
            cl_edit_extra_info.visibility = View.GONE
        }
    }

    private fun setToClipboard(text: String) {
        val clipboardManager =
            getSystemService(requireContext(), ClipboardManager::class.java)
        val clipData = ClipData.newPlainText("Data", text)
        clipboardManager?.setPrimaryClip(clipData)
        Toast.makeText(requireContext(), "Text copied to clipboard", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.isAdding.get()) {
            (activity as MainActivity).hideTabBar()
        }
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).showTabBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditDeviceViewModel::class.java)
        setupEventsObserver()
        arguments?.apply {
            val deviceJson = EditDeviceFragmentArgs.fromBundle(
                arguments
            ).device
            val isAdding = EditDeviceFragmentArgs.fromBundle(
                arguments
            ).stringIsAdding
            viewModel.isAdding.set(isAdding)
            val device = try {
                Gson().fromJson(deviceJson, DeviceResponse::class.java)
            } catch (e: JsonSyntaxException) {
                viewModel.getDeviceById(deviceJson)
            }
            viewModel.device = device
            device?.let {
                if (it.dataFormat == METRIC_TYPE_SINGLE) {
                    viewModel.isSingleValue.set(true)
                    it.metricsConfig?.keys?.firstOrNull()?.let { name ->
                        viewModel.singleMetricName.set(name)
                        viewModel.singleMetricMeasurement.set(
                            it.metricsConfig?.get(name)?.measurementType
                        )
                        viewModel.isSingleValuePublic.set(
                            (it.metricsConfig?.get(name)?.isPublic == true)
                        )
                    }
                }
            }

        }
        if (viewModel.device == null) {
            viewModel.isAdding.set(true)
            viewModel.userGateways.observe(viewLifecycleOwner, Observer {
                viewModel.device = EMPTY_DEVICE.apply {
                    gatewayId = it.firstOrNull()?.gatewayId
                }
            })
        }
        setupExtraInfo()
        header_layout.tv_header_title.text = viewModel.device?.displayName
        header_layout.ib_header_back_arrow.setOnClickListener { findNavController().popBackStack() }
        viewModel.device?.metricsConfig?.let { config ->
            config.keys.forEach { key ->
                viewModel.configViewStateList += JsonMetricViewState().apply {
                    this.name.set(key)
                    this.isPublic.set((config[key]?.isPublic == true))
                    this.measurement.set((config[key]?.measurementType))
                }
            }
        }
        if (viewModel.configViewStateList.isEmpty()) {
            viewModel.configViewStateList += JsonMetricViewState()
        }
        binding.rvEditJsonMetrics.adapter = JsonMetricsAdapter(viewModel.configViewStateList)
        viewModel.networkResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResponse.Success<*> -> getSuccessDialog(
                    requireContext(),
                    getString(R.string.the_device_was_added_now_you_can_track_it)
                ).apply {
                    show()
                    Handler().postDelayed({
                        dismiss()
                        findNavController().popBackStack()
                    }, 2000)
                }
                else -> getErrorDialog(requireContext()).apply {
                    show()
                    Handler().postDelayed({ dismiss() }, 2000)
                }
            }
        })
        viewModel.onMapPickerRequest = { showLocationPicker() }
        viewModel.loadUserGateways()
        viewModel.userGateways.observe(viewLifecycleOwner, Observer {
            val gatewayNames = it.mapNotNull { gateway ->
                gateway.displayName
            }
            binding.spEditGateways.adapter = getSpinnerAdapter(
                (gatewayNames as java.util.List<String>).toArray(
                    Array(gatewayNames.size) { i -> gatewayNames[i] }
                )
            )
        })
        binding.viewModel = viewModel

        viewModel.device?.let { device ->
            requireContext().resources.getStringArray(R.array.data_format_values).firstOrNull()
                ?.let {
                    device.dataFormat = it
                }
        }
        binding.etEditDisplayName.addTextChangedListener { text ->
            try {
                if (text.toString()
                        .isBlank()
                ) throw IllegalArgumentException("Name must not be empty")
                viewModel.device?.let {
                    it.displayName = text.toString()
                }
            } catch (e: Exception) {
                Log.e(TAG, "EditDeviceFragment: latitude", e)
                Snackbar.make(ll_edit_root, "${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
        binding.lat.setText("${viewModel.device?.location?.latitude ?: ""}")
        binding.lon.setText("${viewModel.device?.location?.longitude ?: ""}")
        binding.lat.addTextChangedListener { text ->
            try {
                viewModel.device?.location?.let {
                    val currentLon = it.longitude
                    viewModel.device?.location = com.google.firebase.firestore.GeoPoint(
                        text.toString().toDouble(),
                        currentLon
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "EditDeviceFragment: latitude", e)
                Snackbar.make(binding.llEditRoot, "${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
        binding.lon.addTextChangedListener { text ->
            try {
                viewModel.device?.location?.let {
                    val currentLat = it.latitude
                    viewModel.device?.location = com.google.firebase.firestore.GeoPoint(
                        currentLat,
                        text.toString().toDouble()
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "EditDeviceFragment: longitude", e)
                Snackbar.make(binding.llEditRoot, "${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.tbEditFormat.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.device?.dataFormat = METRIC_TYPE_JSON
            } else {
                viewModel.device?.dataFormat = METRIC_TYPE_SINGLE
            }
        }
        binding.spEditGateways.apply {
            adapter = getSpinnerAdapter(context.resources.getStringArray(R.array.demo_gateways))
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        //NOP
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedGateway = viewModel.userGateways.value?.get(position)
                        viewModel.device?.let {
                            it.gatewayId = selectedGateway?.gatewayId
                        }

                    }
                }
        }
    }

    private fun setupEventsObserver() {
        eventsStream.events.observe(viewLifecycleOwner, Observer {
            if (it is HomeScreenEvent.LOADING) {
                viewModel.isLoading.set(true)
            }
            if (it is HomeScreenEvent.DEFAULT) {
                viewModel.isLoading.set(false)
            }
        })
    }

    private fun getSpinnerAdapter(array: Array<out String>): ArrayAdapter<String> {
        return ArrayAdapter(
            requireContext(),
            R.layout.spinner_item_layout,
            array
        ).apply {
            setDropDownViewResource(R.layout.spinner_item_layout)
        }
    }
}
