package com.griddynamics.connectedapps.di

import com.google.gson.GsonBuilder
import com.griddynamics.connectedapps.di.gateway.LocalStorageModule
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.gateway.network.api.NetworkResponseAdapterFactory
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

const val BASE_URL =
    "https://europe-west3-gd-gcp-rnd-connected-apps.cloudfunctions.net/connectedApps/"
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
                    .addHeader("Authorization", "Bearer ${localStorage.getFirebaseToken()}")
                    .build()
                return chain.proceed(newRequest)
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }


    @Provides
    @Singleton
    @JvmStatic
    fun provideLiveDataFactory(localStorage: LocalStorage): NetworkResponseAdapterFactory {
        return NetworkResponseAdapterFactory(localStorage)
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideRetrofit(
        client: OkHttpClient,
        callAdapterFactory: NetworkResponseAdapterFactory
    ): Retrofit {
        val builder = GsonBuilder()
            .serializeNulls()
            .serializeSpecialFloatingPointValues()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create(builder))
            .build()
    }
}
