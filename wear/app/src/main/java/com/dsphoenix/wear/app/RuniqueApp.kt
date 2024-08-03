package com.dsphoenix.wear.app

import android.app.Application
import com.dsphoenix.core.connectivity.data.di.coreConnectivityDataModule
import com.dsphoenix.wear.app.di.appModule
import com.dsphoenix.wear.run.data.di.wearRunDataModule
import com.dsphoenix.wear.run.presentation.di.wearRunPresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RuniqueApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            modules(
                appModule,
                wearRunPresentationModule,
                wearRunDataModule,
                coreConnectivityDataModule
            )
        }
    }
}
