package com.dsphoenix.run.presentation.di

import com.dsphoenix.run.presentation.run_overview.RunOverviewViewModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val runViewModelModule = module {
    viewModelOf(::RunOverviewViewModule)
}
