package com.griddynamics.connectedapps.di.gateway

import com.griddynamics.connectedapps.gateway.stream.DeviceStream
import com.griddynamics.connectedapps.gateway.stream.DeviceStreamImpl
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
}