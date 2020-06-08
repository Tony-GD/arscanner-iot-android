package com.griddynamics.connectedapps.model.settings

data class SettingsDeviceItem(
    val id: String,
    val type: Int,
    val displayName: String,
    var address: String
) {
    companion object {
        const val TYPE_DEVICE = 111
        const val TYPE_GATEWAY = 112
    }
}