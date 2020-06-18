package com.griddynamics.connectedapps.ui.gateway

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.griddynamics.connectedapps.repository.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.repository.stream.DeviceStream
import com.griddynamics.connectedapps.repository.stream.GatewayStream
import com.griddynamics.connectedapps.model.DefaultScannersResponse
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.ui.gateway.events.GatewayDetailsEventsStream
import com.griddynamics.connectedapps.ui.gateway.events.GatewayDetailsScreenEvent
import com.griddynamics.connectedapps.util.MapUtil
import javax.inject.Inject

private const val TAG: String = "GatewayDetailsViewModel"

class GatewayDetailsViewModel @Inject constructor(
    private val gatewayStream: GatewayStream,
    private val deviceStream: DeviceStream,
    private val eventsStream: GatewayDetailsEventsStream
) : ViewModel() {

    fun onOpenDeviceDetails(deviceId: String, address: String) {
        eventsStream.event.value =
            GatewayDetailsScreenEvent.NavigateToDeviceDetailsScreenEvent(deviceId, address)
    }

    fun getGatewayDevices(gatewayId: String): List<DeviceResponse> =
        deviceStream.publicDevices.value?.filter { it.gatewayId == gatewayId } ?: emptyList()

    fun getGateway(gatewayId: String): GatewayResponse? {
        return gatewayStream.gatewayData.value?.firstOrNull {
            it.gatewayId == gatewayId
        }
    }

    fun getAddress(geoPoint: GeoPoint?): LiveData<String> {
        Log.d(TAG, "getAddress() called with: geoPoint = [$geoPoint]")
        return if (geoPoint != null) MapUtil.getAddressFrom(geoPoint)
        else MutableLiveData()
    }

    fun subscribeForMetrics(id: String): LiveData<DefaultScannersResponse> {
        return FirebaseAPI.subscribeForMetrics(id)
    }
}