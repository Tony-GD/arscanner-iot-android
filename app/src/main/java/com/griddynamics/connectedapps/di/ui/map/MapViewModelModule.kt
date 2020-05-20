package com.griddynamics.connectedapps.di.ui.map

import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.di.ViewModelKey
import com.griddynamics.connectedapps.ui.map.MapViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class MapViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindHomeViewModel(viewModel: MapViewModel): ViewModel
}
