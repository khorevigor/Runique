package com.dsphoenix.runique

import android.app.Application
import com.dsphoenix.auth.data.di.authDataModule
import com.dsphoenix.auth.presentation.di.authViewModelModule
import com.dsphoenix.core.data.networking.di.coreDataModule
import com.dsphoenix.run.presentation.di.runViewModelModule
import com.dsphoenix.runique.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            modules(
                authDataModule,
                authViewModelModule,
                appModule,
                coreDataModule,
                runViewModelModule
            )
        }
    }
}
