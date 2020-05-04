package com.griddynamics.connectedapps.di.gateway

import com.griddynamics.connectedapps.gateway.stream.ScannerStream
import com.griddynamics.connectedapps.gateway.stream.ScannerStreamImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object StreamModule {
    @Provides
    @Singleton
    fun provideScannerStream(): ScannerStream {
        return ScannerStreamImpl()
    }
}