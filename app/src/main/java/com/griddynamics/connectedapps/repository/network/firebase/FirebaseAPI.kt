package com.griddynamics.connectedapps.repository.network.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.util.Executors
import com.griddynamics.connectedapps.model.DefaultScannersResponse
import com.griddynamics.connectedapps.model.User
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse
import com.griddynamics.connectedapps.model.device.MetricConfig
import com.griddynamics.connectedapps.repository.local.LocalStorageImpl

private const val TAG: String = "FirebaseAPI"

object FirebaseAPI {
    private val firestore = FirebaseFirestore.getInstance()
    private val localStorage = LocalStorageImpl()

    fun getUserGateways(user: User): LiveData<List<GatewayResponse>> {
        Log.d(TAG, "getUserGateways() called with: user = [$user]")
        FirebaseFirestore.setLoggingEnabled(true)
        val liveData = MutableLiveData<List<GatewayResponse>>()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d(TAG, "getUserGateways: ${uid}")
        val query = firestore.collection("gateways").whereEqualTo("user_id", uid)
        query.addSnapshotListener(
            Executors.BACKGROUND_EXECUTOR,
            gatewayEventListener(liveData)
        )
        return liveData
    }

    fun getUserDevices(user: User): LiveData<List<DeviceResponse>> {
        Log.d(TAG, "getUserDevices() called with: user = [$user]")
        FirebaseFirestore.setLoggingEnabled(true)
        val liveData = MutableLiveData<List<DeviceResponse>>()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val query = firestore.collection("devices").whereEqualTo("user_id", uid)
        query.addSnapshotListener(
            Executors.BACKGROUND_EXECUTOR,
            deviceEventListener(liveData)
        )
        return liveData
    }

    fun getPublicDevices(): LiveData<List<DeviceResponse>> {
        Log.d(TAG, "getPublicDevices() called")
        FirebaseFirestore.setLoggingEnabled(true)
        val liveData = MutableLiveData<List<DeviceResponse>>()
        val query = firestore.collection("/devices")
        query.addSnapshotListener(
            Executors.BACKGROUND_EXECUTOR,
            deviceEventListener(liveData)
        )
        return liveData
    }

    private fun deviceEventListener(callback: MutableLiveData<List<DeviceResponse>>): EventListener<QuerySnapshot> {
        return EventListener { snapshot, exception ->
            Log.d(
                TAG,
                "deviceEventListener() called with: snapshot = [${snapshot}], exception = [$exception]"
            )
            val devices = mutableListOf<DeviceResponse>()
            snapshot?.documents?.forEach {
                Log.d(TAG, "getPublicDevices: ${it.data}")
                val displayName = it.data?.get("display_name")
                val dataFormat = it.data?.get("data_format")
                val updatedAt = it.data?.get("updated_at")
                val createdAt = it.data?.get("created_at")
                val userId = it.data?.get("user_id")
                val location = it.data?.get("location")
                val gatewayId = it.data?.get("gateway_id")
                val locationDescription = it.data?.get("location_description")
                val publicMetrics = it.data?.get("publicMetrics")
                val metricsConfig = it.data?.get("metricsConfig") as Map<String, Map<String, Any>>?
                val deviceId = it.reference.path.split("/").last()
                val device = DeviceResponse(
                    deviceId = deviceId,
                    displayName = displayName as String?,
                    dataFormat = dataFormat as String?,
                    updateAt = updatedAt as Timestamp?,
                    userId = userId as String?,
                    location = location as GeoPoint?,
                    locationDescription = locationDescription as String?,
                    createdAt = createdAt as Timestamp?,
                    gatewayId = gatewayId as String?,
                    jsonMetricsField = null,
                    metrics = null,
                    publicMetrics = publicMetrics as List<String>?,
                    metricsConfig = (mutableMapOf<String, MetricConfig>().apply {
                        metricsConfig?.keys?.forEach { key ->
                            val tempMetric = metricsConfig[key]
                            tempMetric?.let {
                                val realMetric: MetricConfig =
                                    MetricConfig(
                                        isPublic = tempMetric["is_public"] as Boolean?,
                                        measurementType = tempMetric["measurementType"] as String?
                                    )
                                this[key] = realMetric
                            }
                        }
                    })
                )
                Log.d(TAG, "getPublicDevices: $device")
                devices.add(device)
            }
            callback.postValue(devices.filterPublicAndUserDevices())
        }
    }

    fun getPublicGateways(): LiveData<List<GatewayResponse>> {
        Log.d(TAG, "getPublicGateways() called")
        FirebaseFirestore.setLoggingEnabled(true)
        val liveData = MutableLiveData<List<GatewayResponse>>()
        val query = firestore.collection("/gateways")
        query.addSnapshotListener(
            Executors.BACKGROUND_EXECUTOR,
            gatewayEventListener(liveData)
        )
        return liveData
    }

    private fun gatewayEventListener(callback: MutableLiveData<List<GatewayResponse>>): EventListener<QuerySnapshot> {
        return EventListener { snapshot, exception ->
            val gateways = mutableListOf<GatewayResponse>()
            snapshot?.documents?.forEach {
                Log.d(TAG, "gatewayEventListener: ${it.data}")
                val gatewayId = it.reference.path.split("/").last()
                val userId = it.data?.get("user_id")
                val displayName = it.data?.get("display_name")
                val key = it.data?.get("key")
                val gateway = GatewayResponse(
                    gatewayId = gatewayId,
                    userId = userId as String?,
                    displayName = displayName as String?,
                    key = key as String?
                )
                Log.d(TAG, "gatewayEventListener: $gateway")
                gateways.add(gateway)
                callback.postValue(gateways)
            }
            exception?.let {
                Log.e(TAG, "FirebaseAPI: gatewayEventListener", it)
            }

        }
    }

    private fun Any.getSafeNumber(): Float {
        return try {
            (this as Double).toFloat()
        } catch (e: ClassCastException) {
            (this as Long).toFloat()
        }
    }

    fun subscribeForMetrics(id: String): LiveData<DefaultScannersResponse> {
        Log.d(TAG, "subscribeForMetrics() called with: id = [$id]")
        val liveData = MutableLiveData<DefaultScannersResponse>()
        firestore
            .collection("devices")
            .document(id)
            .collection("metrics")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e(TAG, "FirebaseAPI: addSnapshotListener", firebaseFirestoreException)
                    return@addSnapshotListener
                }
                querySnapshot?.documents?.forEach { document ->
                    val metricName = document.reference.path.split("/").last()
                    val response = DefaultScannersResponse(metricName, 0f)
                    document.data?.let {
                        when (metricName) {
                            "[default]" -> {
                                response.value = it.values.last().getSafeNumber()
                            }
                            else -> {
                                response.value = (it.values.last() as Number).toFloat()
                            }
                        }
                        Log.d(TAG, "subscribeForMetrics: response $response")
                        liveData.value = response
                    }
                }
            }
        return liveData
    }

    private fun List<DeviceResponse>.filterPublicAndUserDevices(): List<DeviceResponse> {
        return this.filter { device ->
            device.publicMetrics?.isNotEmpty() == true
                    || device.userId == localStorage.getUser().uid
        }
    }
}