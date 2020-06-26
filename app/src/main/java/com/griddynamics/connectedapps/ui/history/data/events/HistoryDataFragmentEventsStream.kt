package com.griddynamics.connectedapps.ui.history.data.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.home.edit.events.HomeScreenEvent

interface HistoryDataFragmentEventsStream {
    val events: MutableLiveData<HomeScreenEvent>
}