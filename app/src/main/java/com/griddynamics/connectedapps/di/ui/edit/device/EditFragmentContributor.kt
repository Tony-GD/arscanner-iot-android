package com.griddynamics.connectedapps.di.ui.edit.device

import com.griddynamics.connectedapps.ui.edit.device.EditDeviceFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class EditFragmentContributor {
    @ContributesAndroidInjector
    abstract fun contributeFragment(): EditDeviceFragment
}