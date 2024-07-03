package com.dsphoenix.analytics.presentation.di

import com.dsphoenix.analytics.presentation.AnalyticsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val analyticsPresentationModule = module {
    viewModelOf(::AnalyticsViewModel)
}
