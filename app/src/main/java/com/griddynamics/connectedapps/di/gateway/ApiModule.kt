package com.griddynamics.connectedapps.di.gateway

import com.griddynamics.connectedapps.gateway.api.AirScannerAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object ApiModule {
    @Provides
    @JvmStatic
    fun provideAirScannerApi(retrofit: Retrofit): AirScannerAPI {
        return retrofit.create(AirScannerAPI::class.java)
    }

    @Provides
    @JvmStatic
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://wikipedia.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}