package com.dsphoenix.run.network.di

import com.dsphoenix.core.domain.run.RemoteImageDataSource
import com.dsphoenix.core.domain.run.RemoteRunDataSource
import com.dsphoenix.run.network.FirebaseStorageImageDataSource
import com.dsphoenix.run.network.FirestoreRemoteRunDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    singleOf(::FirestoreRemoteRunDataSource).bind<RemoteRunDataSource>()
    singleOf(::FirebaseStorageImageDataSource).bind<RemoteImageDataSource>()
}
