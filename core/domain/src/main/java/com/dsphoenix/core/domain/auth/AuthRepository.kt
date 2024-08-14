package com.dsphoenix.core.domain.auth

import com.dsphoenix.core.domain.util.EmptyResult

interface AuthRepository {

    fun isUserLoggedIn(): Boolean
    fun getUserId(): String
    suspend fun login(email: String, password: String): EmptyResult<AuthError>
    suspend fun register(email: String, password: String): EmptyResult<AuthError>
    fun logout()
}
