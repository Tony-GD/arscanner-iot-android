package com.griddynamics.connectedapps.di.ui.gateway

import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.di.ViewModelKey
import com.griddynamics.connectedapps.ui.gateway.GatewayDetailsViewModel
import com.griddynamics.connectedapps.ui.history.HistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GatewayDetailsViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(GatewayDetailsViewModel::class)
    abstract fun bindHomeViewModel(viewModel: GatewayDetailsViewModel): ViewModel
}