package com.griddynamics.connectedapps.di.ui.edit.gateway

import com.griddynamics.connectedapps.ui.edit.gateway.EditGatewayFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class EditGatewayFragmentContributor {
    @ContributesAndroidInjector
    abstract fun contributeFragment(): EditGatewayFragment
}