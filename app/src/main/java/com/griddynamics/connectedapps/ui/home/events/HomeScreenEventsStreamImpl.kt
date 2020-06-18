package com.griddynamics.connectedapps.ui.home.events

import androidx.lifecycle.MutableLiveData

class HomeScreenEventsStreamImpl: HomeScreenEventsStream {
    override val events: MutableLiveData<HomeScreenEvent> = MutableLiveData()
}