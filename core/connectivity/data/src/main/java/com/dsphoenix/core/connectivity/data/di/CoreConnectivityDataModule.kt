package com.dsphoenix.core.connectivity.data.di

import com.dsphoenix.core.connectivity.data.WearNodeDiscovery
import com.dsphoenix.core.connectivity.data.messaging.WearMessagingClient
import com.dsphoenix.core.connectivity.domain.NodeDiscovery
import com.dsphoenix.core.connectivity.domain.messaging.MessagingClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreConnectivityDataModule = module {
    singleOf(::WearMessagingClient).bind<MessagingClient>()
    singleOf(::WearNodeDiscovery).bind<NodeDiscovery>()
}
