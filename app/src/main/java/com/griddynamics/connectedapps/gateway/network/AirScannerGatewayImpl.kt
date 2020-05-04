package com.griddynamics.connectedapps.gateway.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.gateway.api.AirScannerAPI
import com.griddynamics.connectedapps.gateway.stream.ScannerStream
import com.griddynamics.connectedapps.model.ScannersResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG: String = "AirScannerGatewayImpl"
@Singleton
class AirScannerGatewayImpl
@Inject constructor(private val api: AirScannerAPI, private val stream: ScannerStream) : AirScannerGateway {
    private var data =  MutableLiveData<String>()
    private val job = Job()
    private val scope = CoroutineScope(GlobalScope.coroutineContext + job)
    override fun getAirScanners(): LiveData<ScannersResponse> {
        val data = MutableLiveData<ScannersResponse>()
        scope.launch{
            val result = runAsync().await()
            data.postValue(result.body())
        }
        return data
    }

    override fun getName(): LiveData<String> {
        Log.d(TAG, "getName() called")
        scope.launch {
            stream.scannerData.postValue("Data 1")
            data.postValue("Data 1")
        }
                return data
    }

    override fun getName2() {
        Log.d(TAG, "getName2: ${stream}")
        stream.scannerData.postValue("Data 2")
    }

    private fun runAsync() = runBlocking {
        val def = async {
            api.getScanners()
        }
        def
    }
}