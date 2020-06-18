package com.griddynamics.connectedapps.ui.home.events

sealed class HomeScreenEvent {
    object DEFAULT: HomeScreenEvent()
    object LOADING: HomeScreenEvent()
}