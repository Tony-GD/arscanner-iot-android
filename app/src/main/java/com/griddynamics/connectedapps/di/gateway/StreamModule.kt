package com.griddynamics.connectedapps.di.gateway

import com.griddynamics.connectedapps.gateway.stream.DeviceStream
import com.griddynamics.connectedapps.gateway.stream.DeviceStreamImpl
import com.griddynamics.connectedapps.gateway.stream.MetricsStream
import com.griddynamics.connectedapps.gateway.stream.MetricsStreamImpl
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
}