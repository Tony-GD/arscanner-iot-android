package com.griddynamics.connectedapps.di.ui.main

import com.griddynamics.connectedapps.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityContributor {
    @ContributesAndroidInjector
    abstract fun contributeFragment(): MainActivity
}