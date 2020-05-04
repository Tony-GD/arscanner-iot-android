package com.griddynamics.connectedapps.di.ui.home

import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.di.ViewModelKey
import com.griddynamics.connectedapps.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class HomeViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
}
