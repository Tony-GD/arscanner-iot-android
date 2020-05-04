package com.griddynamics.connectedapps.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object RetrofitModule {
    @Provides
    @JvmStatic
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("")
            .build()
    }
}