package com.dsphoenix.run.presentation.di

import com.dsphoenix.run.domain.RunningTracker
import com.dsphoenix.run.presentation.active_run.ActiveRunViewModel
import com.dsphoenix.run.presentation.run_overview.RunOverviewViewModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val runPresentationModule = module {
    singleOf(::RunningTracker)

    viewModelOf(::RunOverviewViewModule)
    viewModelOf(::ActiveRunViewModel)
}
