package com.griddynamics.connectedapps.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.databinding.MapInfoViewLayoutBinding
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.ui.map.bottomsheet.BottomSheetDeviceDetailsFragment
import com.griddynamics.connectedapps.util.getMapColorFilter
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_map.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject

private const val TAG: String = "MapFragment"

class MapFragment : DaggerFragment() {

    interface OnDeviceSelectedListener {
        fun onDeviceSelected(device: DeviceResponse)
        fun onDeviceEditSelected(device: DeviceResponse)
    }

    interface OnGatewaySelectedListener {
        fun onGatewaySelected(gateway: GatewayResponse)
    }

    private lateinit var mapViewModel: MapViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val onDeviceSelectedListener = object : OnDeviceSelectedListener {
        override fun onDeviceSelected(device: DeviceResponse) {
            navigateToHistoryFragment("${device.deviceId}")
        }

        override fun onDeviceEditSelected(device: DeviceResponse) {
            navigateToEditFragment(device)
        }
    }

    private val onGatewaySelectedListener = object : OnGatewaySelectedListener {
        override fun onGatewaySelected(gateway: GatewayResponse) {
            navigateToGatewayEditFragment("${gateway.displayName}")
        }
    }

    private val locationChangeListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val mapController = map.controller as MapController
            val startPoint = GeoPoint(location.latitude, location.longitude)
            mapController.animateTo(startPoint)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            //NOP
        }

        override fun onProviderEnabled(provider: String?) {
            //NOP
        }

        override fun onProviderDisabled(provider: String?) {
            //NOP
        }
    }

    private fun navigateToEditFragment(data: DeviceResponse) {
        val actionGlobalNavigationEdit = MapFragmentDirections.ActionNavigationMapToNavigationEdit()
        actionGlobalNavigationEdit.setDevice(Gson().toJson(data))
        actionGlobalNavigationEdit.setStringIsAdding(false)
        findNavController().navigate(actionGlobalNavigationEdit)
    }

    private fun navigateToHistoryFragment(data: String) {
        val actionGlobalNavigationHistory =
            MapFragmentDirections.ActionNavigationMapToNavigationHistory()
        actionGlobalNavigationHistory.setDevice(data)
        findNavController().navigate(actionGlobalNavigationHistory)
    }

    private fun navigateToGatewayEditFragment(data: String) {
        val actionGlobalNavigationHistory =
            MapFragmentDirections.ActionNavigationMapToEditNavigationGatewayGateway()
        actionGlobalNavigationHistory.setGateway(data)
        findNavController().navigate(actionGlobalNavigationHistory)
    }

    private fun generateMarker(
        device: DeviceResponse,
        onMarkerReadyCallback: (marker: Marker) -> Unit
    ) {
        device.location?.let {
            val binding: MapInfoViewLayoutBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(requireContext()),
                    R.layout.map_info_view_layout,
                    map,
                    false
                )
            binding.item = device
//            binding.listener = onDeviceSelectedListener
            val greenIcon = requireContext().getDrawable(R.drawable.green)
            val selectedIcon = requireContext().getDrawable(R.drawable.green_selected)
            val elementMarker = Marker(map)
            elementMarker.setOnMarkerClickListener { selectedMarker, mapView ->
                Log.d(TAG, "generateMarker: inCLick $selectedMarker")
                selectedMarker.icon = selectedIcon
                showDetails(device)
                mapView.overlays.forEach { overlay ->
                    if (overlay is Marker && overlay != selectedMarker) {
                        overlay.icon = greenIcon
                    }
                }
                true
            }
            elementMarker.icon = greenIcon
            elementMarker.infoWindow = null
            elementMarker.position = GeoPoint(it.latitude, it.longitude)
            elementMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            onMarkerReadyCallback(elementMarker)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        mapViewModel =
            ViewModelProvider(this, viewModelFactory).get(MapViewModel::class.java)

        mapViewModel.loadDevices()

        mapViewModel.loadGateways()
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ctx: Context = requireActivity().applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        super.onViewCreated(view, savedInstanceState)
        mapViewModel.devices.observe(viewLifecycleOwner, Observer {
            map.overlays.clear()
            it.forEach { device ->
                generateMarker(device) { marker ->
                    map.overlays.add(marker)
                }
            }
        })
        setupMap()
    }

    private fun showDetails(device: DeviceResponse) {
        Log.d(TAG, "showDetails() called with: device = [$device]")
        val bottomSheetDialogFragment =
            BottomSheetDeviceDetailsFragment().apply { setDeviceInfo(device) }
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
    }

    private fun setupMap() {
        map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController = map.controller as MapController
        map.setBuiltInZoomControls(true)
        mapController.zoomTo(10)
        map.setUseDataConnection(true)
        map.setMultiTouchControls(true)
        map.overlayManager.tilesOverlay
            .setColorFilter(
                getMapColorFilter(
                    requireContext().getColor(R.color.colorMapBlack)
                )
            )
        Dexter.withContext(activity)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val locationManager =
                            getSystemService(requireContext(), LocationManager::class.java)

                        locationManager?.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            5000L,
                            10f,
                            locationChangeListener
                        )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.permission_message),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.permission_message),
                        Toast.LENGTH_LONG
                    ).show()
                    permissionToken.continuePermissionRequest()
                }

            })
            .check();

    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

}
