package com.griddynamics.connectedapps.ui.home.edit.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.common.ScreenEvent

interface EditDeviceScreenEventsStream {
    val events: MutableLiveData<ScreenEvent>
}