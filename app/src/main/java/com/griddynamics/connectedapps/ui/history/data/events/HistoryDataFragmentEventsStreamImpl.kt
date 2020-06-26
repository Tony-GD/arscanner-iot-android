package com.griddynamics.connectedapps.ui.history.data.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.history.data.events.HistoryDataFragmentEventsStream
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEvent

class HistoryDataFragmentEventsStreamImpl: HistoryDataFragmentEventsStream {
    override val events: MutableLiveData<HomeScreenEvent> = MutableLiveData()
}