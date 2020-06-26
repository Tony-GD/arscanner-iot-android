package com.griddynamics.connectedapps.ui.history.data.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.home.edit.events.HomeScreenEvent

class HistoryDataFragmentEventsStreamImpl: HistoryDataFragmentEventsStream {
    override val events: MutableLiveData<HomeScreenEvent> = MutableLiveData()
}