package com.griddynamics.connectedapps.di.gateway

import com.griddynamics.connectedapps.gateway.network.AirScannerGateway
import com.griddynamics.connectedapps.gateway.network.AirScannerGatewayImpl
import dagger.Module
import dagger.Provides

@Module(includes = [ApiModule::class, LocalStorageModule::class])
 object GatewayModule {
    @Provides
    @JvmStatic
     fun provideGateway(gateway: AirScannerGatewayImpl): AirScannerGateway {
        return gateway
    }
}