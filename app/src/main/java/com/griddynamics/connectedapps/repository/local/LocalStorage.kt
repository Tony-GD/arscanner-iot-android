package com.griddynamics.connectedapps.repository.local

import com.griddynamics.connectedapps.model.User

interface LocalStorage {
    fun saveUser(user: User)
    fun getUser(): User
    fun saveFirebaseToken(token: String?)
    fun getFirebaseToken(): String?
    fun saveWidgetTrackedDevice(deviceId: String?)
    fun getWidgetTrackedDevice(): String?
    fun clear()
}