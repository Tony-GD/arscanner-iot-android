package com.griddynamics.connectedapps.ui.home.edit.events

import androidx.lifecycle.MutableLiveData

class EditDeviceScreenEventsStreamImpl: EditDeviceScreenEventsStream {
    override val events: MutableLiveData<HomeScreenEvent> = MutableLiveData()
}