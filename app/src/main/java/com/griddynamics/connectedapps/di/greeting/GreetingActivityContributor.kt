package com.griddynamics.connectedapps.di.greeting

import com.griddynamics.connectedapps.MainActivity
import com.griddynamics.connectedapps.ui.greeting.GreetingActivity
import dagger.Module

import dagger.android.ContributesAndroidInjector


@Module
abstract class GreetingActivityContributor {
    @ContributesAndroidInjector
    abstract fun contributeActivityInjector(): GreetingActivity
}