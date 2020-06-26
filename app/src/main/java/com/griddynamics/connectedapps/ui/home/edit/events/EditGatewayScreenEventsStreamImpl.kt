package com.griddynamics.connectedapps.ui.home.edit.events

import androidx.lifecycle.MutableLiveData

class EditGatewayScreenEventsStreamImpl: EditGatewayScreenEventsStream {
    override val events: MutableLiveData<HomeScreenEvent> = MutableLiveData()
}