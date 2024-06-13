package com.dsphoenix.core.data.networking.di

import com.dsphoenix.core.data.networking.HttpClientFactory
import com.dsphoenix.core.data.networking.auth.EncryptedSessionStorage
import com.dsphoenix.core.domain.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}
