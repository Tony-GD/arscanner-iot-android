package com.griddynamics.connectedapps.ui.home.edit.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.common.ScreenEvent

class EditGatewayScreenEventsStreamImpl: EditGatewayScreenEventsStream {
    override val events: MutableLiveData<ScreenEvent> = MutableLiveData()
}