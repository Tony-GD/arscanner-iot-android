package com.griddynamics.connectedapps.di

import com.griddynamics.connectedapps.di.gateway.LocalStorageModule
import com.griddynamics.connectedapps.gateway.api.LiveDataCallAdapterFactory
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "http://192.168.0.101"
private const val TAG: String = "RetrofitModule"

@Module(includes = [LocalStorageModule::class])
object RetrofitModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideOkHttpClient(localStorage: LocalStorage): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val authInterceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${localStorage.getUser().id}")
                    .build()
                return chain.proceed(newRequest)
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
//            .addInterceptor(authInterceptor)
            .build()
    }


    @Provides
    @Singleton
    @JvmStatic
    fun provideLiveDataFactory(): LiveDataCallAdapterFactory {
        return LiveDataCallAdapterFactory()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideRetrofit(
        liveDataCallAdapterFactory: LiveDataCallAdapterFactory,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(liveDataCallAdapterFactory)
            .build()
    }
}
