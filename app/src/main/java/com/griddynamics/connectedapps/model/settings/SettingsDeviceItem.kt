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



    override fun toString(): String {
        return "SettingsDeviceItem(id='$id', type=$type, displayName='$displayName', address='$address')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SettingsDeviceItem

        if (id != other.id) return false
        if (type != other.type) return false
        if (displayName != other.displayName) return false
        if (address != other.address) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + type
        result = 31 * result + displayName.hashCode()
        result = 31 * result + address.hashCode()
        return result
    }
}