package com.dsphoenix.auth.data.di

import com.dsphoenix.auth.data.EmailPatternValidator
import com.dsphoenix.auth.data.FirebaseAuthRepository
import com.dsphoenix.core.domain.auth.AuthRepository
import com.dsphoenix.auth.domain.PatternValidator
import com.dsphoenix.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {

    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::FirebaseAuthRepository).bind<AuthRepository>()
}
