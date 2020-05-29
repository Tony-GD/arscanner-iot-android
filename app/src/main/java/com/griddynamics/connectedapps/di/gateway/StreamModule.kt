package com.griddynamics.connectedapps.di.gateway

import com.griddynamics.connectedapps.gateway.stream.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object StreamModule {
    @Provides
    @Singleton
    fun provideScannerStream(): DeviceStream {
        return DeviceStreamImpl()
    }

    @Provides
    @Singleton
    fun provideMetricsStream() : MetricsStream {
        return MetricsStreamImpl()
    }

    @Provides
    @Singleton
    fun provideGatewaysStream() : GatewayStream {
        return GatewayStreamImpl()
    }
}