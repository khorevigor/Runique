package com.dsphoenix.wear.run.data.di

import com.dsphoenix.wear.run.data.HealthServicesExerciseTracker
import com.dsphoenix.wear.run.domain.ExerciseTracker
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val wearRunDataModule = module {
    singleOf(::HealthServicesExerciseTracker).bind<ExerciseTracker>()
}
