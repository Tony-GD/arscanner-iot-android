package com.griddynamics.connectedapps.di.ui.history

import com.griddynamics.connectedapps.ui.history.day.events.DayHistoryEventsStream
import com.griddynamics.connectedapps.ui.history.day.events.DayHistoryEventsStreamImpl
import com.griddynamics.connectedapps.ui.history.hour.events.HourHistoryEventsStream
import com.griddynamics.connectedapps.ui.history.hour.events.HourHistoryEventsStreamImpl
import com.griddynamics.connectedapps.ui.history.week.events.WeekHistoryEventsStream
import com.griddynamics.connectedapps.ui.history.week.events.WeekHistoryEventsStreamImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object HistoryScreenEventsModule {
    @Provides
    @Singleton
    fun provideDayEventsStream(): DayHistoryEventsStream {
        return DayHistoryEventsStreamImpl()
    }

    @Provides
    @Singleton
    fun provideHourEventsStream(): HourHistoryEventsStream {
        return HourHistoryEventsStreamImpl()
    }

    @Provides
    @Singleton
    fun provideWeekEventsStream(): WeekHistoryEventsStream {
        return WeekHistoryEventsStreamImpl()
    }
}