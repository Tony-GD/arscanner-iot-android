package com.griddynamics.connectedapps.di.ui.gateway.events

import com.griddynamics.connectedapps.ui.gateway.events.GatewayDetailsEventsStream
import com.griddynamics.connectedapps.ui.gateway.events.GatewayDetailsEventsStreamImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object GatewayDetailsEventsModule {
    @Provides
    @Singleton
    fun provideEventsStream(): GatewayDetailsEventsStream {
        return GatewayDetailsEventsStreamImpl()
    }
}