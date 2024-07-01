package com.dsphoenix.core.domain.run

import kotlin.time.Duration

interface SyncRunScheduler {

    suspend fun scheduleSync(syncType: SyncType)
    suspend fun cancelAllSyncs()

    sealed interface SyncType {
        data class FetchRuns(val interval: Duration): SyncType
        data class DeleteRun(val runId: RunId): SyncType
        class CreateRun(val run: Run, val mapPictureBytes: ByteArray): SyncType
    }
}
