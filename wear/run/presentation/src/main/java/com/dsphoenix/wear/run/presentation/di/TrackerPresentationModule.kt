package com.dsphoenix.wear.run.presentation.di

import com.dsphoenix.wear.run.presentation.TrackerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val runPresentationModule = module {
    viewModelOf(::TrackerViewModel)
}
