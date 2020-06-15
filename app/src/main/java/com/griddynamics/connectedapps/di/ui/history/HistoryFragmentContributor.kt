package com.griddynamics.connectedapps.di.ui.history

import com.griddynamics.connectedapps.ui.history.HistoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HistoryFragmentContributor {
    @ContributesAndroidInjector
    abstract fun contributeFragment(): HistoryFragment
}