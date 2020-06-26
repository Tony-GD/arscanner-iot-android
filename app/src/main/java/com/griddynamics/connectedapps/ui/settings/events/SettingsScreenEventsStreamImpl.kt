package com.griddynamics.connectedapps.ui.settings.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.common.ScreenEvent

class SettingsScreenEventsStreamImpl: SettingsScreenEventsStream {
    override val events: MutableLiveData<ScreenEvent> = MutableLiveData()
}