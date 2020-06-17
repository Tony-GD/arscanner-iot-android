package com.griddynamics.connectedapps.di.ui.gateway

import com.griddynamics.connectedapps.ui.gateway.GatewayDetailsFragment
import dagger.Module

import dagger.android.ContributesAndroidInjector;

@Module
public abstract class GatewayDetailsFragmentContributor {
    @ContributesAndroidInjector
    abstract fun contributeFragment(): GatewayDetailsFragment
}
