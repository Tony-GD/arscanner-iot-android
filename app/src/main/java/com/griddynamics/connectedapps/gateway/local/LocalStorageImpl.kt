package com.griddynamics.connectedapps.gateway.local

import android.util.Log
import com.griddynamics.connectedapps.model.EmptyUser
import com.griddynamics.connectedapps.model.User
import com.orhanobut.hawk.Hawk

private const val TAG: String = "LocalStorageImpl"
private const val USER_KEY = "USER_KEY"
private const val FIREBASE_TOKEN_KEY = "FIREBASE_TOKEN_KEY"
class LocalStorageImpl: LocalStorage {
    override fun saveUser(user: User) {
        Log.d(TAG, "saveUser() called with: user = [$user]")
        Hawk.put(USER_KEY, user)
    }

    override fun saveFirebaseToken(token: String?) {
        Hawk.put(FIREBASE_TOKEN_KEY, token)
    }

    override fun getFirebaseToken(): String? {
        return Hawk.get(FIREBASE_TOKEN_KEY)
    }

    override fun getUser(): User {
        return Hawk.get(USER_KEY) ?: EmptyUser
    }
}