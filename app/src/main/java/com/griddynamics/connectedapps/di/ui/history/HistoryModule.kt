package com.griddynamics.connectedapps.di.ui.history

import dagger.Module

@Module(includes = [HistoryViewModelModule::class, HistoryScreenEventsModule::class])
interface HistoryModule