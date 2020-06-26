package com.griddynamics.connectedapps.di.ui.settings

import com.griddynamics.connectedapps.ui.settings.events.SettingsScreenEventsStream
import com.griddynamics.connectedapps.ui.settings.events.SettingsScreenEventsStreamImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object SettingsEventsModule {
    @Provides
    @Singleton
    fun provideSettingsEventsStream(): SettingsScreenEventsStream {
        return SettingsScreenEventsStreamImpl()
    }
}