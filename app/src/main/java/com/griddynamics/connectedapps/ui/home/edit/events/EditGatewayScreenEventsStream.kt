package com.griddynamics.connectedapps.ui.home.edit.events

import androidx.lifecycle.MutableLiveData

interface EditGatewayScreenEventsStream {
    val events: MutableLiveData<HomeScreenEvent>
}