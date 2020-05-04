package com.griddynamics.connectedapps.di

import androidx.lifecycle.ViewModelProvider
import com.griddynamics.connectedapps.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
