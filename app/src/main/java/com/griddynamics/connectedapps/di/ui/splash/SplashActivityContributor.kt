package com.griddynamics.connectedapps.di.ui.splash

import com.griddynamics.connectedapps.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class SplashActivityContributor {
    @ContributesAndroidInjector
    abstract fun contributeActivityInjector(): SplashActivity
}