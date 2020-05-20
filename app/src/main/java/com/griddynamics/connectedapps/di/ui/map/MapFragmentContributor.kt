package com.griddynamics.connectedapps.di.ui.map

import com.griddynamics.connectedapps.ui.map.MapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MapFragmentContributor {
    @ContributesAndroidInjector
    abstract fun contributeFragment(): MapFragment
}