package com.griddynamics.connectedapps.di.ui.home.edit.gateway

import com.griddynamics.connectedapps.ui.home.edit.gateway.EditGatewayFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class EditGatewayFragmentContributor {
    @ContributesAndroidInjector
    abstract fun contributeFragment(): EditGatewayFragment
}