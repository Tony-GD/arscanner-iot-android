package com.griddynamics.connectedapps.di.ui.settings

import dagger.Module

@Module(includes = [SettingsViewModelModule::class, SettingsEventsModule::class])
interface SettingsModule