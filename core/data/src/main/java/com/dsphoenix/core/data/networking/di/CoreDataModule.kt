package com.dsphoenix.core.data.networking.di

import com.dsphoenix.core.data.networking.HttpClientFactory
import com.dsphoenix.core.data.networking.auth.EncryptedSessionStorage
import com.dsphoenix.core.data.networking.run.ConnectivityChecker
import com.dsphoenix.core.data.networking.run.OfflineFirstRunRepository
import com.dsphoenix.core.domain.SessionStorage
import com.dsphoenix.core.domain.run.RunRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
    singleOf(::ConnectivityChecker)
}
