package com.dsphoenix.core.data.networking.di

import com.dsphoenix.core.data.networking.HttpClientFactory
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory().build()
    }
}
