package com.griddynamics.connectedapps.ui.history.data.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.common.ScreenEvent

class HistoryDataFragmentEventsStreamImpl: HistoryDataFragmentEventsStream {
    override val events: MutableLiveData<ScreenEvent> = MutableLiveData()
}