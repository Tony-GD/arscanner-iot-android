package com.griddynamics.connectedapps.di.ui.greeting

import com.griddynamics.connectedapps.ui.greeting.GreetingActivity
import dagger.Module

import dagger.android.ContributesAndroidInjector


@Module
abstract class GreetingActivityContributor {
    @ContributesAndroidInjector
    abstract fun contributeActivityInjector(): GreetingActivity
}