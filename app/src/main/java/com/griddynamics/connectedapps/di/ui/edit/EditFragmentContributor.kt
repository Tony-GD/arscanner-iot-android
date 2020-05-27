package com.griddynamics.connectedapps.di.ui.edit

import com.griddynamics.connectedapps.MainActivity
import com.griddynamics.connectedapps.ui.edit.EditDeviceFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class EditFragmentContributor {
    @ContributesAndroidInjector
    abstract fun contributeFragment(): EditDeviceFragment
}