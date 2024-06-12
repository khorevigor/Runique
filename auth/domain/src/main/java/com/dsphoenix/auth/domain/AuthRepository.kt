package com.dsphoenix.auth.domain

import com.dsphoenix.core.domain.util.DataError
import com.dsphoenix.core.domain.util.EmptyResult

interface AuthRepository {

    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>
}
