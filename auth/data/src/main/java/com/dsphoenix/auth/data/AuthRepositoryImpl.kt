package com.dsphoenix.auth.data

import com.dsphoenix.auth.domain.AuthRepository
import com.dsphoenix.core.data.networking.post
import com.dsphoenix.core.domain.util.DataError
import com.dsphoenix.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient
): AuthRepository {

    override suspend fun register(email: String, password: String): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }
}
