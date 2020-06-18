package com.griddynamics.connectedapps.ui.gateway

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.griddynamics.connectedapps.gateway.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.gateway.stream.DeviceStream
import com.griddynamics.connectedapps.gateway.stream.GatewayStream
import com.griddynamics.connectedapps.model.DefaultScannersResponse
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.util.MapUtil
import javax.inject.Inject

class GatewayDetailsViewModel @Inject constructor(
    private val gatewayStream: GatewayStream,
    private val deviceStream: DeviceStream
) : ViewModel() {

    fun getGatewayDevices(gatewayId: String): List<DeviceResponse> =
        deviceStream.publicDevices.value?.filter { it.gatewayId == gatewayId } ?: emptyList()

    fun getGateway(gatewayId: String): GatewayResponse? {
        return gatewayStream.gatewayData.value?.firstOrNull { it.gatewayId == gatewayId }
    }

    fun getAddress(geoPoint: GeoPoint?): LiveData<String> =
        if (geoPoint != null) MapUtil.getAddressFrom(geoPoint)
        else MutableLiveData()

    fun subscribeForMetrics(id: String): LiveData<DefaultScannersResponse> {
        return FirebaseAPI.subscribeForMetrics(id)
    }
}