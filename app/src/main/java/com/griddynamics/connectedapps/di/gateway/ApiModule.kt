package com.griddynamics.connectedapps.di.gateway

import com.griddynamics.connectedapps.di.RetrofitModule
import com.griddynamics.connectedapps.gateway.api.AirScannerAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [RetrofitModule::class])
object ApiModule {
    @Provides
    @JvmStatic
    fun provideAirScannerApi(retrofit: Retrofit): AirScannerAPI {
        return retrofit.create(AirScannerAPI::class.java)
    }
}