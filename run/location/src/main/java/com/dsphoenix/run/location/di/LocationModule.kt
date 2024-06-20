package com.dsphoenix.run.location.di

import com.dsphoenix.run.domain.LocationObserver
import com.dsphoenix.run.location.AndroidLocationObserver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val locationModule = module {
    singleOf(::AndroidLocationObserver).bind<LocationObserver>()
}
