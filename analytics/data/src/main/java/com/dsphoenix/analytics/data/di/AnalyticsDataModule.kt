package com.dsphoenix.analytics.data.di

import com.dsphoenix.analytics.data.RoomAnalyticsRepository
import com.dsphoenix.analytics.domain.AnalyticsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsModule = module {
    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()
}
