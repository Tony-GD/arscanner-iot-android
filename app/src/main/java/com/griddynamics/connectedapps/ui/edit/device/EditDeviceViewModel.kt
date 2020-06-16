package com.griddynamics.connectedapps.ui.edit.device

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.gateway.network.AirScannerRepository
import com.griddynamics.connectedapps.gateway.network.api.NetworkResponse
import com.griddynamics.connectedapps.gateway.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.gateway.stream.DeviceStream
import com.griddynamics.connectedapps.model.device.*
import com.griddynamics.connectedapps.model.metrics.JsonMetricViewState
import com.griddynamics.connectedapps.ui.home.Callback
import kotlinx.coroutines.launch
import javax.inject.Inject

const val CO2 = "CO2"
const val TEMP = "Temperature"
const val TEMP_SHORT = "Temp"
const val HUMIDITY = "Humidity"
const val PM2_5 = "PM2,5"
const val PM2 = "pm2"
const val PM1_0 = "PM1,0"
const val PM1 = "pm1"
const val PM10 = "PM10"
const val DEFAULT = "[default]"

private const val TAG: String = "EditDeviceViewModel"

class EditDeviceViewModel @Inject constructor(
    private val deviceStream: DeviceStream,
    private val localStorage: LocalStorage,
    private val repository: AirScannerRepository
) :
    ViewModel() {
    var device: DeviceResponse? = null
    val configViewStateList = mutableListOf<JsonMetricViewState>()
    val isSingleValue = ObservableBoolean(false)
    val isAdding = ObservableBoolean()
    val observableKey = ObservableField<String>(
//        "@�;y\u0002\u0003\u0001\u0000\u0001\u0002�\u0001\u0000\u001A�xkQ,���1\u0011.\t�;-n9�D�>��fR��РN,�����\u0007�\u001C��\u001C=��\u001F���\u0018�)1�?\u0000�:\u0016���ks�\u0019��GG(X+����@J?:���~�xV6'tU��d��\u001E��y\u0082�n��\u0019#r%\u000EL&\$Z�{\u007F��No��z�AW���5\u001B��\u0012.8�~��+�\u0017�U\n" +
//                "��ۉ�\u0015�=\u0002l���ܮ�\u0001XQ��\$�_�{jr_�!\t��\n" +
//                "C\u0001��m���`ޑզW�\u0000ks��\n" +
//                "��D�\u0017\u001C_{\u001F%([�i\u0015\u001A\u001Dv���\u0005,{\u000F�u�}Ft\u0017Җ\u0017����^y\u001A+mA���NE�\u000FK�\u001Bߑ�\u0013��G�u�K�9��\u0018\u0018�j�8�\u000B��d�\u0013*��\u0002��\u0002��\u0000ڪ� ��m��\u001B��\\f\u0006�@/(ؽ��_�.MXXW�!��I�b�q�ݒ�,�;���w�\u0006b��?:*�4|.Q4ĵ\u0002��\u0013�^\\f����\u0013b#C?8\u000Fj*<L��X���c\u0019\u001F�8�3�d��SU@�f�\u000B�vOx���\u001B�\u000B\u0015\u0002��eX���n. �<�X\n" +
//                ".����;��`�Gh\u000F�\\fY\n" +
//                "�Ն�Ę�πM�������Jʜ:�W�\u0003��[]�\t�\u001D8��\u001CR��y9��R�S\u001FƂ���hM�\u0012�OVn)�t6F]ĕ�v�[\u001E�C��sD��릹�q���FZD�\u0002��\u0000ٙG��.9\u0015JO��X)�|s��&\u0002%u:�%F���\\f��nQfzt�&9�`��z��\u007F���SJ<����`Pnw%\u0006n�v;ub��\u0003�zy�\u001A���)RB�K���ֿ@\u007F\u001F�NqU\u001D.ʈ;\u001D�\"m��\u0018�y��Z��=�q�;�2\u0015\u0002��p�R��e�����t�G�E��/�)�͌�\u0006�f��\u001E\u0016x\u0016o�\u0019���rv%�p�\u0016&��/�\u0007\u0007\$Z\u0019(�\u0000ʘ�(�8a�|�\"����KlC�Ք�{��VF���Rc�6������\u001D\u0012\u0012�mR%g�2r\u0010#�N[��j�ԃ\u0014��"
    )
    var onMapPickerRequest: Callback? = null
    val singleMetricName = ObservableField<String>()
    val isSingleValuePublic = ObservableBoolean()
    val singleMetricMeasurement = ObservableField<String>()
    val networkResponse = MutableLiveData<NetworkResponse<Any, Any>>()

    val userGateways = MediatorLiveData<List<GatewayResponse>>()

    fun getDeviceById(id: String): DeviceResponse {
        lateinit var deviceById: DeviceResponse
        deviceStream.publicDevices.value?.forEach { device ->
            if (device.deviceId == id) {
                deviceById = device
            }
        }
        return deviceById
    }

    fun loadUserGateways() {
        userGateways.addSource(FirebaseAPI.getUserGateways(localStorage.getUser()))
        { gateways ->
            userGateways.value = gateways
        }
    }

    private fun loadUserDevices() {
        Log.d(TAG, "loadUserDevices() called")
        deviceStream.userDevices.addSource(FirebaseAPI.getUserDevices(localStorage.getUser()))
        { devices ->
            deviceStream.userDevices.value = devices
        }
    }

    fun saveEditedDevice() {
        viewModelScope.launch {
            device?.let {
                if (isAdding.get()) {
                    val response = repository.addDevice(it.apply { saveMetrics() })
                    networkResponse.postValue(response)
                    when (response) {
                        is NetworkResponse.Success<*> -> {
                            Log.d(TAG, "saveAddedDevice success: ${response.body}")
                            loadUserDevices()
                        }
                        else -> Log.e(TAG, "EditDeviceViewModel: error ${response}")
                    }
                } else {
                    val response = repository.editDevice(it.apply { saveMetrics() })
                    networkResponse.postValue(response)
                    when (response) {
                        is NetworkResponse.Success<*> -> {
                            Log.d(TAG, "saveEditedDevice success: ${response.body}")
                            loadUserDevices()
                        }
                        else -> Log.e(TAG, "EditDeviceViewModel: error ${response}")
                    }
                }
            }
        }
    }

    private fun DeviceResponse.saveMetrics() {

        val tempMetricsConfig = mutableMapOf<String, MetricConfig>()
        val tempPublicMetrics = mutableListOf<String>()
        if (isSingleValue.get()) {
            tempMetricsConfig[singleMetricName.get() ?: DEFAULT] =
                MetricConfig(isSingleValuePublic.get(), "${singleMetricMeasurement.get()}")
            if (isSingleValuePublic.get()) {
                tempPublicMetrics += "${singleMetricName.get()}"
            }
        } else {
            configViewStateList.forEach { config ->
                tempMetricsConfig["${config.name.get()}"] =
                    MetricConfig(config.isPublic.get(), "${config.measurement.get()}")
                if (config.isPublic.get()) {
                    tempPublicMetrics += "${config.name.get()}"
                }
            }
        }
        this.dataFormat = if (isSingleValue.get()) METRIC_TYPE_SINGLE else METRIC_TYPE_JSON
        this.metricsConfig = tempMetricsConfig
        this.publicMetrics = tempPublicMetrics
        this.key = observableKey.get()
    }

    fun deleteDevice() {
        viewModelScope.launch {
            device?.let {
                repository.deleteDevice("${it.deviceId}")
            }
        }
    }

}
