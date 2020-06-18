package com.griddynamics.connectedapps.ui.history.hour.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEvent

class HourHistoryEventsStreamImpl: HourHistoryEventsStream {
    override val events: MutableLiveData<HomeScreenEvent> = MutableLiveData()
}