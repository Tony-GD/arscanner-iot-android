package com.griddynamics.connectedapps.ui.common

sealed class ScreenEvent {
    object DEFAULT: ScreenEvent()
    object LOADING: ScreenEvent()
    object SUCCESS: ScreenEvent()
    object ERROR: ScreenEvent()
}