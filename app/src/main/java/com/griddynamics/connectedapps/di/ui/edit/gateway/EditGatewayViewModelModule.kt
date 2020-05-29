package com.griddynamics.connectedapps.di.ui.edit.gateway

import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.di.ViewModelKey
import com.griddynamics.connectedapps.ui.edit.gateway.EditGatewayViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class EditGatewayViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(EditGatewayViewModel::class)
    abstract fun bindEditViewModel(viewModel: EditGatewayViewModel): ViewModel
}
