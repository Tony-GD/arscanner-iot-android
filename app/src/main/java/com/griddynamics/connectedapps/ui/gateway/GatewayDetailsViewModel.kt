package com.griddynamics.connectedapps.ui.gateway

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.GeoPoint
import com.griddynamics.connectedapps.model.DefaultScannersResponse
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.repository.network.AirScannerRepository
import com.griddynamics.connectedapps.repository.network.api.NetworkResponse
import com.griddynamics.connectedapps.repository.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.repository.stream.DeviceStream
import com.griddynamics.connectedapps.repository.stream.GatewayStream
import com.griddynamics.connectedapps.ui.gateway.events.GatewayDetailsEventsStream
import com.griddynamics.connectedapps.ui.gateway.events.GatewayDetailsScreenEvent
import com.griddynamics.connectedapps.util.MapUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG: String = "GatewayDetailsViewModel"

class GatewayDetailsViewModel @Inject constructor(
    private val repository: AirScannerRepository,
    private val gatewayStream: GatewayStream,
    private val deviceStream: DeviceStream,
    private val eventsStream: GatewayDetailsEventsStream
) : ViewModel() {

    var gateway: GatewayResponse? = null

    fun loadDevices() {
        val liveData = MediatorLiveData<List<DeviceResponse>>()
        liveData.addSource(FirebaseAPI.getPublicDevices(), Observer {
            liveData.value = it
            deviceStream.publicDevices.value = it
        })
    }

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

    fun deleteGateway() {
        gateway?.let { gateway ->
            viewModelScope.launch {
                val response = repository.deleteGateway("${gateway.gatewayId}")
                Log.d(TAG, "deleteGateway: $response")
                when (response) {
                    is NetworkResponse.Success -> {
                        eventsStream.event.postValue(GatewayDetailsScreenEvent.SUCCESS)
                    }
                    is NetworkResponse.ApiError -> {
                        eventsStream.event.postValue(
                            GatewayDetailsScreenEvent.Error(
                                response.body.toString().replace("}", "").split("=").last()
                            )
                        )
                    }
                    else -> {
                        eventsStream.event.postValue(GatewayDetailsScreenEvent.Error(null))
                    }
                }
            }
        }
    }
}