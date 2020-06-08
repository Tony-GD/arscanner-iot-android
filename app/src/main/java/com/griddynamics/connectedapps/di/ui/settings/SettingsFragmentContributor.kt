package com.griddynamics.connectedapps.di.ui.settings

import com.griddynamics.connectedapps.ui.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsFragmentContributor {
    @ContributesAndroidInjector
    abstract fun contributeFragment(): SettingsFragment
}