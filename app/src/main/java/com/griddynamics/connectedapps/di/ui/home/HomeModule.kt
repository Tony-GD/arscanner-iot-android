package com.griddynamics.connectedapps.di.ui.home

import com.griddynamics.connectedapps.di.ui.home.events.HomeScreenEventsModule
import dagger.Module

@Module(includes = [HomeViewModelModule::class, HomeScreenEventsModule::class])
interface HomeModule