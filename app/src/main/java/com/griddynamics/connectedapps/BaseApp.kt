package com.griddynamics.connectedapps

import com.griddynamics.connectedapps.di.ApplicationComponent
import com.griddynamics.connectedapps.di.DaggerApplicationComponent
import com.orhanobut.hawk.Hawk
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject


class BaseApp: DaggerApplication() {
    @Inject lateinit var childFragmentInjector: DispatchingAndroidInjector<BaseApp>

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        Hawk.init(applicationContext).build()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
       applicationComponent = DaggerApplicationComponent.builder().application(this).build()
        return applicationComponent
    }

}