package com.griddynamics.connectedapps.di.ui.home.events

import com.griddynamics.connectedapps.ui.home.edit.events.EditDeviceScreenEventsStream
import com.griddynamics.connectedapps.ui.home.edit.events.EditDeviceScreenEventsStreamImpl
import com.griddynamics.connectedapps.ui.home.edit.events.EditGatewayScreenEventsStream
import com.griddynamics.connectedapps.ui.home.edit.events.EditGatewayScreenEventsStreamImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object HomeScreenEventsModule {
    @Provides
    @Singleton
    fun provideGatewayEventsStream(): EditGatewayScreenEventsStream {
        return EditGatewayScreenEventsStreamImpl()
    }

    @Provides
    @Singleton
    fun provideDeviceEventsStream(): EditDeviceScreenEventsStream {
        return EditDeviceScreenEventsStreamImpl()
    }
}