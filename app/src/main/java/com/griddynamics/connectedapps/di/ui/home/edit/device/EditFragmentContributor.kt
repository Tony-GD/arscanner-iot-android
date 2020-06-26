package com.griddynamics.connectedapps.di.ui.home.edit.device

import com.griddynamics.connectedapps.ui.home.edit.device.EditDeviceFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class EditFragmentContributor {
    @ContributesAndroidInjector
    abstract fun contributeFragment(): EditDeviceFragment
}