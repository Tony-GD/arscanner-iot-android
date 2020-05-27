package com.griddynamics.connectedapps.ui.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.databinding.EditDeviceFragmentBinding
import com.griddynamics.connectedapps.model.device.*
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
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

    companion object {
        fun newInstance() =
            EditDeviceFragment()
    }


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: EditDeviceFragmentBinding
    private lateinit var viewModel: EditDeviceViewModel
    private var selectedLat: Float = 0f
    private var selectedLong: Float = 0f

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
                selectedLat = p.latitude.toFloat()
                selectedLong = p.longitude.toFloat()
                view.map_picker.overlays.clear()
                val elementMarker = Marker(view.map_picker)
                elementMarker.position = p
                elementMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                view.map_picker.overlays.add(elementMarker)
                return false
            }
        }

        val mapEventsOverlay = MapEventsOverlay(eventsReceiver)
        setupMap(view.map_picker)
        view.map_picker.overlays.add(mapEventsOverlay)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
        view.picker_ok_btn.setOnClickListener {
            binding.lat.setText(this.selectedLat.toString())
            binding.lon.setText(this.selectedLong.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setupMap(map: MapView) {
        map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController = map.controller as MapController
        map.setBuiltInZoomControls(true)
        mapController.zoomTo(10)
        map.setUseDataConnection(true)
        map.setMultiTouchControls(true)
        val startPoint = GeoPoint(DEFAULT_LAT.toDouble(), DEFAULT_LONG.toDouble())
        mapController.animateTo(startPoint)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(
            TAG,
            "onViewCreated() called with: view = [$view], savedInstanceState = [$savedInstanceState]"
        )
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditDeviceViewModel::class.java)
        viewModel.onMapPickerRequest = { showLocationPicker() }
        binding.viewModel = viewModel
        arguments?.apply {
            val device = EditDeviceFragmentArgs.fromBundle(
                arguments
            ).device
            viewModel.device = Gson().fromJson(device, DeviceResponse::class.java)
            viewModel.deviceRequest = EMPTY_DEVICE_REQUEST.copy(deviceId = device)
            viewModel.editDevice.observe(viewLifecycleOwner, Observer {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
            })
        }
    }

}
