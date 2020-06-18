package com.griddynamics.connectedapps.ui.history.week.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEvent

class WeekHistoryEventsStreamImpl : WeekHistoryEventsStream {
    override val events: MutableLiveData<HomeScreenEvent> = MutableLiveData()
}