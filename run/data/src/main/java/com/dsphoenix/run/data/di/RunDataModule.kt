package com.dsphoenix.run.data.di

import com.dsphoenix.run.data.CreateRunWorker
import com.dsphoenix.run.data.DeleteRunsWorker
import com.dsphoenix.run.data.FetchRunsWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunsWorker)
}
