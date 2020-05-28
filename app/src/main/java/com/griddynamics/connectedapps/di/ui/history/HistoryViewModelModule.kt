package com.griddynamics.connectedapps.di.ui.history

import androidx.lifecycle.ViewModel
import com.griddynamics.connectedapps.di.ViewModelKey
import com.griddynamics.connectedapps.ui.history.HistoryFragmentViewModel
import com.griddynamics.connectedapps.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class HistoryViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HistoryFragmentViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HistoryFragmentViewModel): ViewModel
}
