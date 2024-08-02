package com.dsphoenix.run.data.di

import com.dsphoenix.core.domain.run.SyncRunScheduler
import com.dsphoenix.run.data.CreateRunWorker
import com.dsphoenix.run.data.DeleteRunWorker
import com.dsphoenix.run.data.FetchRunsWorker
import com.dsphoenix.run.data.SyncRunWorkerScheduler
import com.dsphoenix.run.data.connectivity.PhoneToWatchConnector
import com.dsphoenix.run.domain.WatchConnector
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)

    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()
    singleOf(::PhoneToWatchConnector).bind<WatchConnector>()
}
