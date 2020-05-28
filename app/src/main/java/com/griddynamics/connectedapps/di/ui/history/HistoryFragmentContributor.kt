package com.griddynamics.connectedapps.di.ui.history

import com.griddynamics.connectedapps.ui.history.DataHistoryFragment
import com.griddynamics.connectedapps.ui.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HistoryFragmentContributor {
    @ContributesAndroidInjector
    abstract fun contributeFragment(): DataHistoryFragment
}