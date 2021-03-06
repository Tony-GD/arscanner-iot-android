package com.griddynamics.connectedapps.ui.history.data.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.common.ScreenEvent

interface HistoryDataFragmentEventsStream {
    val events: MutableLiveData<ScreenEvent>
}