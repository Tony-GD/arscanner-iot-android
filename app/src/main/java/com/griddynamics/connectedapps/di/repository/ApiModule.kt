package com.griddynamics.connectedapps.di.repository

import com.griddynamics.connectedapps.di.RetrofitModule
import com.griddynamics.connectedapps.repository.network.api.AirScannerAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [RetrofitModule::class])
object ApiModule {
    @Provides
    @JvmStatic
    fun provideAirScannerApi(retrofit: Retrofit): AirScannerAPI {
        return retrofit.create(AirScannerAPI::class.java)
    }
}