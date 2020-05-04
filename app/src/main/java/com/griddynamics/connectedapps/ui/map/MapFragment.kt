package com.griddynamics.connectedapps.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.griddynamics.connectedapps.R
import kotlinx.android.synthetic.main.fragment_map.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController


class MapFragment : Fragment() {

    private lateinit var mapViewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProviders.of(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_map, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ctx: Context = requireActivity().applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        super.onViewCreated(view, savedInstanceState)
        map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController = map.controller as MapController

        val startPoint = GeoPoint(52370816, 9735936)
        mapController.animateTo(startPoint)
        map.setBuiltInZoomControls(true)
        mapController.setZoom(10)
        map.setUseDataConnection(true)
        map.setMultiTouchControls(true)
        requestPermissionsIfNecessary(
            arrayOf(
                // if you need to show the current location, uncomment the line below
                // Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        );
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        val permissionsToRequest: ArrayList<String> = ArrayList()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toArray(arrayOfNulls(0)),
                192
            )
        }
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
