package com.griddynamics.connectedapps.di.gateway

import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.gateway.local.LocalStorageImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LocalStorageModule {
    @Provides
    @Singleton
    fun provideLocalStorageModule(): LocalStorage {
        return LocalStorageImpl()
    }
}