package com.griddynamics.connectedapps.ui.home.events

import androidx.lifecycle.MutableLiveData

interface HomeScreenEventsStream {
    val events: MutableLiveData<HomeScreenEvent>
}