package com.griddynamics.connectedapps.di.repository

import com.griddynamics.connectedapps.repository.network.AirScannerRepository
import com.griddynamics.connectedapps.repository.network.AirScannerRepositoryImpl
import dagger.Module
import dagger.Provides

@Module(includes = [ApiModule::class, LocalStorageModule::class])
 object RepositoryModule {
    @Provides
    @JvmStatic
     fun provideGateway(gateway: AirScannerRepositoryImpl): AirScannerRepository {
        return gateway
    }
}