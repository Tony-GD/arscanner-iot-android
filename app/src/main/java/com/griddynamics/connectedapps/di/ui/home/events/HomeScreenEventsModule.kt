package com.griddynamics.connectedapps.di.ui.home.events

import com.griddynamics.connectedapps.ui.home.events.HomeScreenEventsStream
import com.griddynamics.connectedapps.ui.home.events.HomeScreenEventsStreamImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object HomeScreenEventsModule {
    @Provides
    @Singleton
    fun provideEventsStream(): HomeScreenEventsStream {
        return HomeScreenEventsStreamImpl()
    }
}