package com.griddynamics.connectedapps.ui.home.edit.events

sealed class HomeScreenEvent {
    object DEFAULT: HomeScreenEvent()
    object LOADING: HomeScreenEvent()
    object SUCCESS: HomeScreenEvent()
    object ERROR: HomeScreenEvent()
}