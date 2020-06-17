package com.griddynamics.connectedapps.di

import android.app.Application
import com.griddynamics.connectedapps.BaseApp
import com.griddynamics.connectedapps.di.repository.RepositoryModule
import com.griddynamics.connectedapps.di.repository.StreamModule
import com.griddynamics.connectedapps.di.ui.edit.device.EditFragmentContributor
import com.griddynamics.connectedapps.di.ui.edit.device.EditModule
import com.griddynamics.connectedapps.di.ui.edit.gateway.EditGatewayFragmentContributor
import com.griddynamics.connectedapps.di.ui.edit.gateway.EditGatewayModule
import com.griddynamics.connectedapps.di.ui.gateway.GatewayDetailsFragmentContributor
import com.griddynamics.connectedapps.di.ui.gateway.GatewayDetailsModule
import com.griddynamics.connectedapps.di.ui.greeting.GreetingActivityContributor
import com.griddynamics.connectedapps.di.ui.history.HistoryFragmentContributor
import com.griddynamics.connectedapps.di.ui.history.HistoryModule
import com.griddynamics.connectedapps.di.ui.home.HomeFragmentContributor
import com.griddynamics.connectedapps.di.ui.home.HomeModule
import com.griddynamics.connectedapps.di.ui.main.MainActivityContributor
import com.griddynamics.connectedapps.di.ui.map.MapFragmentContributor
import com.griddynamics.connectedapps.di.ui.map.MapModule
import com.griddynamics.connectedapps.di.ui.settings.SettingsFragmentContributor
import com.griddynamics.connectedapps.di.ui.settings.SettingsModule
import com.griddynamics.connectedapps.di.ui.splash.SplashActivityContributor
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        RepositoryModule::class,
        StreamModule::class,
        ViewModelFactoryModule::class,
        HomeModule::class,
        HomeFragmentContributor::class,
        MapModule::class,
        MapFragmentContributor::class,
        GreetingActivityContributor::class,
        SplashActivityContributor::class,
        HistoryModule::class,
        HistoryFragmentContributor::class,
        EditModule::class,
        EditFragmentContributor::class,
        EditGatewayModule::class,
        SettingsModule::class,
        SettingsFragmentContributor::class,
        EditGatewayFragmentContributor::class,
        MainActivityContributor::class,
        GatewayDetailsFragmentContributor::class,
        GatewayDetailsModule::class]
)
interface ApplicationComponent : AndroidInjector<BaseApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }
}