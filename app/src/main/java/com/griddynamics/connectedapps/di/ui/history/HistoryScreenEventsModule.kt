package com.griddynamics.connectedapps.di.ui.history

import com.griddynamics.connectedapps.ui.history.data.events.HistoryDataFragmentEventsStream
import com.griddynamics.connectedapps.ui.history.data.events.HistoryDataFragmentEventsStreamImpl
import com.griddynamics.connectedapps.ui.history.events.HistoryFragmentEventsStream
import com.griddynamics.connectedapps.ui.history.events.HistoryFragmentEventsStreamImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object HistoryScreenEventsModule {
    @Provides
    @Singleton
    fun provideDataHistoryEventsStream(): HistoryDataFragmentEventsStream {
        return HistoryDataFragmentEventsStreamImpl()
    }

    @Provides
    @Singleton
    fun provideHistoryEventsStream(): HistoryFragmentEventsStream {
        return HistoryFragmentEventsStreamImpl()
    }
}