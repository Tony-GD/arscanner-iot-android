package com.griddynamics.connectedapps.di.ui.edit

import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.di.ViewModelKey
import com.griddynamics.connectedapps.ui.edit.EditDeviceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class EditViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(EditDeviceViewModel::class)
    abstract fun bindEditViewModel(viewModel: EditDeviceViewModel): ViewModel
}
