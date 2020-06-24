package com.griddynamics.connectedapps.ui.history.events

import androidx.lifecycle.MutableLiveData

class HistoryFragmentEventsStreamImpl : HistoryFragmentEventsStream {
    override val events = MutableLiveData<HistoryFragmentEvent>()
}