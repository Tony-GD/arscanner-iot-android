package com.griddynamics.connectedapps.util

import android.location.Address
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.location.GeocoderNominatim

object MapUtil {
    private const val TAG: String = "MapUtil"
    private val geocoder = GeocoderNominatim(GeocoderNominatim.NOMINATIM_SERVICE_URL)

    suspend fun getAddress(location: GeoPoint): String {
        return try {
            downloadInfo(location).firstOrNull()?.getAddressLine()!!
        } catch (e: Exception) {
            Log.e(TAG, "MapUtil: getAddressFrom:", e)
            return "Address is unavailable"
        }
    }

    fun getAddressFrom(location: GeoPoint): LiveData<String> {
        val data = MutableLiveData<String>()
        GlobalScope.launch {
            try {
                downloadInfo(location).firstOrNull()?.let { address ->
                    data.postValue(address.getAddressLine())
                }
            } catch (e: Exception) {
                Log.e(TAG, "MapUtil: getAddressFrom:", e)
                data.postValue("Address temporary unavailable")
            }
        }
        return data
    }

    private fun downloadInfo(location: GeoPoint): List<Address> {
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
    }

    private fun Address.getAddressLine(): String {
        val sb = StringBuilder()
        if (this.maxAddressLineIndex < 2) {
            sb.append(this.getAddressLine(0)).append(", ")
            sb.append(this.subAdminArea)
        } else {
            for (i in 0..this.maxAddressLineIndex) {
                sb.append(this.getAddressLine(i))
                if (i < this.maxAddressLineIndex) {
                    sb.append(", ")
                }
            }
        }
        return sb.toString()
    }
}
