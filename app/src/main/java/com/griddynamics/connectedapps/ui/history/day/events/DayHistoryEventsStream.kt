package com.griddynamics.connectedapps.ui.history.day.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEvent

interface DayHistoryEventsStream {
    val events: MutableLiveData<HomeScreenEvent>
}