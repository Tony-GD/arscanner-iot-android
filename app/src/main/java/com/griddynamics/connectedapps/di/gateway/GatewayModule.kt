package com.griddynamics.connectedapps.di.gateway

import com.griddynamics.connectedapps.gateway.network.AirScannerRepository
import com.griddynamics.connectedapps.gateway.network.AirScannerRepositoryImpl
import dagger.Module
import dagger.Provides

@Module(includes = [ApiModule::class, LocalStorageModule::class])
 object GatewayModule {
    @Provides
    @JvmStatic
     fun provideGateway(gateway: AirScannerRepositoryImpl): AirScannerRepository {
        return gateway
    }
}