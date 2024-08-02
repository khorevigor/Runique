package com.dsphoenix.wear.app.di

import com.dsphoenix.wear.app.RuniqueApp
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single {
        (androidApplication() as RuniqueApp).applicationScope
    }
}
