package com.griddynamics.connectedapps.di

import android.app.Application
import com.griddynamics.connectedapps.BaseApp
import com.griddynamics.connectedapps.di.gateway.GatewayModule
import com.griddynamics.connectedapps.di.gateway.StreamModule
import com.griddynamics.connectedapps.di.home.HomeFragmentContributor
import com.griddynamics.connectedapps.di.home.HomeModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        GatewayModule::class,
        StreamModule::class,
        HomeModule::class,
        ViewModelFactoryModule::class,
        HomeFragmentContributor::class]
)
interface ApplicationComponent: AndroidInjector<BaseApp>{

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }
}