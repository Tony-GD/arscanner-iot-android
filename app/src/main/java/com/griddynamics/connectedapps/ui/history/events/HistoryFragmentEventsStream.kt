package com.griddynamics.connectedapps.ui.history.events

import androidx.lifecycle.MutableLiveData

interface HistoryFragmentEventsStream {
    val events: MutableLiveData<HistoryFragmentEvent>
}