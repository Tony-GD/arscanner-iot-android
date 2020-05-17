package com.griddynamics.connectedapps.di

import com.griddynamics.connectedapps.gateway.api.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://europe-west3-gd-gcp-rnd-connected-apps.cloudfunctions.net"

@Module
object RetrofitModule {
    @Provides
    @JvmStatic
    fun provideLiveDataFactory(): LiveDataCallAdapterFactory {
        return LiveDataCallAdapterFactory()
    }

    @Provides
    @JvmStatic
    fun provideRetrofit(liveDataCallAdapterFactory: LiveDataCallAdapterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(liveDataCallAdapterFactory)
            .build()
    }
}
