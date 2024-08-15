package com.dsphoenix.core.domain.run

import com.dsphoenix.core.domain.util.DataError
import com.dsphoenix.core.domain.util.Result

interface RemoteImageDataSource {
    suspend fun uploadImage(runId: RunId, image: ByteArray): Result<String, DataError.Network>
}
