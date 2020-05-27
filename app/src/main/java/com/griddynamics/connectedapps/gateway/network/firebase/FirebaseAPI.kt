package com.griddynamics.connectedapps.gateway.network.firebase

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.util.Executors
import com.griddynamics.connectedapps.model.User
import com.griddynamics.connectedapps.model.device.DeviceResponse
import com.griddynamics.connectedapps.model.device.GatewayResponse

private const val TAG: String = "FirebaseAPI"
typealias DevicesCallback = (devices: List<DeviceResponse>) -> Unit
typealias GatewaysCallback = (gateways: List<GatewayResponse>) -> Unit

object FirebaseAPI {
    private val firestore = FirebaseFirestore.getInstance()

    fun getUserGateways(user: User, callback: GatewaysCallback) {
        Log.d(TAG, "getUserGateways() called with: user = [$user]")
        FirebaseFirestore.setLoggingEnabled(true)
        val query = firestore.collection("gateways").whereEqualTo("user_id", user.id)
        query.addSnapshotListener(
            Executors.BACKGROUND_EXECUTOR,
            gatewayEventListener(callback)
        )
    }

    fun getUserDevices(user: User, callback: DevicesCallback) {
        Log.d(TAG, "getUserDevices() called with: user = [$user]")
        FirebaseFirestore.setLoggingEnabled(true)
        val query = firestore.collection("devices").whereEqualTo("user_id", user.id)
        query.addSnapshotListener(
            Executors.BACKGROUND_EXECUTOR,
            deviceEventListener(callback)
        )
    }

    fun getPublicDevices(callback: DevicesCallback) {
        Log.d(TAG, "getPublicDevices() called")
        FirebaseFirestore.setLoggingEnabled(true)
        val query = firestore.collection("/devices")
        query.addSnapshotListener(
            Executors.BACKGROUND_EXECUTOR,
            deviceEventListener(callback)
        )
    }

    private fun deviceEventListener(callback: DevicesCallback): EventListener<QuerySnapshot> {
        return EventListener { snapshot, exception ->
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
                    gatewayId = gatewayId as String?
                )
                Log.d(TAG, "getPublicDevices: $device")
                devices.add(device)
            }
            callback(devices)
        }
    }

    fun getPublicGateways(callback: GatewaysCallback) {
        Log.d(TAG, "getPublicGateways() called")
        FirebaseFirestore.setLoggingEnabled(true)
        val query = firestore.collection("/gateways")
        query.addSnapshotListener(
            Executors.BACKGROUND_EXECUTOR,
            gatewayEventListener(callback)
        )
    }

    private fun gatewayEventListener(callback: GatewaysCallback): EventListener<QuerySnapshot> {
        return EventListener { snapshot, exception ->
            val gateways = mutableListOf<GatewayResponse>()
            snapshot?.documents?.forEach {
                Log.d(TAG, "getPublicGateways: ${it.data}")
                val userId = it.data?.get("user_id")
                val displayName = it.data?.get("display_name")
                val gateway = GatewayResponse(
                    userId = userId as String?,
                    displayName = displayName as String?
                )
                Log.d(TAG, "getPublicGateways: $gateway")
                gateways.add(gateway)
            }
            callback(gateways)
        }
    }
}