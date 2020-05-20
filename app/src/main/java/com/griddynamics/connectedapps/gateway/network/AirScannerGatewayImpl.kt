package com.griddynamics.connectedapps.gateway.network

import android.util.Log
import androidx.lifecycle.LiveData
import com.griddynamics.connectedapps.gateway.api.*
import com.griddynamics.connectedapps.gateway.stream.DeviceStream
import com.griddynamics.connectedapps.model.EMPTY_DEVICES_RESPONSE
import com.griddynamics.connectedapps.model.GetDevicesResponse
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG: String = "AirScannerGatewayImpl"

@Singleton
class AirScannerGatewayImpl
@Inject constructor(private val api: AirScannerAPI, private val stream: DeviceStream) :
    AirScannerGateway {
    private val job = Job()
    private val scope = CoroutineScope(GlobalScope.coroutineContext + job)

    override fun getAirScanners(): LiveData<ApiResponse<GetDevicesResponse>> {
        Log.d(TAG, "getAirScanners() called")
        stream.scannerData.postValue(ApiSuccessResponse(EMPTY_DEVICES_RESPONSE))
            return api.getScanners()
    }

    private fun runAsync() = runBlocking {
        val def = async {
            api.getScanners()
        }
        def
    }
}