package com.griddynamics.connectedapps.di.ui.home

import com.griddynamics.connectedapps.ui.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeFragmentContributor {
    @ContributesAndroidInjector
    abstract fun contributeFragment(): HomeFragment
}