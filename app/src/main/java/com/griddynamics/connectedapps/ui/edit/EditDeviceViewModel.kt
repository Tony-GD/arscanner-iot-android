package com.griddynamics.connectedapps.ui.edit

import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.model.DeviceRequest
import com.griddynamics.connectedapps.ui.home.Callback

class EditDeviceViewModel : ViewModel() {
    var device: DeviceRequest? = null
    var onMapPickerRequest: Callback? = null
}
