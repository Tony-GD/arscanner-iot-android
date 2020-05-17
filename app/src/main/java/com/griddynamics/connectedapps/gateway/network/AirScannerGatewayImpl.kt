package com.griddynamics.connectedapps.gateway.network

import androidx.lifecycle.LiveData
import com.griddynamics.connectedapps.gateway.api.AirScannerAPI
import com.griddynamics.connectedapps.gateway.stream.ScannerStream
import com.griddynamics.connectedapps.model.GetDevicesResponse
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG: String = "AirScannerGatewayImpl"

@Singleton
class AirScannerGatewayImpl
@Inject constructor(private val api: AirScannerAPI, private val stream: ScannerStream) :
    AirScannerGateway {
    private val job = Job()
    private val scope = CoroutineScope(GlobalScope.coroutineContext + job)

    override fun getAirScanners(): LiveData<GetDevicesResponse> {
        val data = stream.scannerData
        scope.launch {
            val result = runAsync().await()
            data.postValue(result.body())
        }
        return data
    }

    private fun runAsync() = runBlocking {
        val def = async {
            api.getScanners()
        }
        def
    }
}