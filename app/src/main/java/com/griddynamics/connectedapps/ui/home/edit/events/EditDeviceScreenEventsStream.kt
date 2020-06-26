package com.griddynamics.connectedapps.ui.home.edit.events

import androidx.lifecycle.MutableLiveData

interface EditDeviceScreenEventsStream {
    val events: MutableLiveData<HomeScreenEvent>
}