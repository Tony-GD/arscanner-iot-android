package com.griddynamics.connectedapps.di.ui.gateway

import com.griddynamics.connectedapps.di.ui.gateway.events.GatewayDetailsEventsModule
import dagger.Module

@Module(includes = [GatewayDetailsViewModelModule::class, GatewayDetailsEventsModule::class])
interface GatewayDetailsModule