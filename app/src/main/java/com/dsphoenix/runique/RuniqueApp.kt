package com.dsphoenix.runique

import android.app.Application
import android.content.Context
import com.dsphoenix.auth.data.di.authDataModule
import com.dsphoenix.auth.presentation.di.authViewModelModule
import com.dsphoenix.core.connectivity.data.di.coreConnectivityDataModule
import com.dsphoenix.core.database.di.databaseModule
import com.dsphoenix.run.data.di.runDataModule
import com.dsphoenix.run.location.di.locationModule
import com.dsphoenix.run.network.di.networkModule
import com.dsphoenix.run.presentation.di.runPresentationModule
import com.dsphoenix.runique.di.appModule
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            workManagerFactory()
            modules(
                authDataModule,
                authViewModelModule,
                appModule,
                runPresentationModule,
                locationModule,
                databaseModule,
                networkModule,
                runDataModule,
                coreConnectivityDataModule
            )
        }

        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}
