package com.griddynamics.connectedapps.di.repository

import com.griddynamics.connectedapps.repository.local.LocalStorage
import com.griddynamics.connectedapps.repository.local.LocalStorageImpl
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