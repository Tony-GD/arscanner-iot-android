package com.griddynamics.connectedapps.ui.settings.events

import androidx.lifecycle.MutableLiveData
import com.griddynamics.connectedapps.ui.common.ScreenEvent

interface SettingsScreenEventsStream {
    val events: MutableLiveData<ScreenEvent>
}