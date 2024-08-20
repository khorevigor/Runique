package com.dsphoenix.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dsphoenix.core.database.dao.RunPendingSyncDao
import com.dsphoenix.core.database.mappers.toRun
import com.dsphoenix.core.domain.run.LocalRunDataSource
import com.dsphoenix.core.domain.run.RemoteRunDataSource

class CreateRunWorker(
    context: Context,
    private val params: WorkerParameters,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val localRunDataSource: LocalRunDataSource,
    private val pendingSyncDao: RunPendingSyncDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val pendingRunId = params.inputData.getString(RUN_ID) ?: return Result.failure()
        val pendingRunEntity =
            pendingSyncDao.getRunPendingSyncEntity(pendingRunId) ?: return Result.failure()

        return when (val result = remoteRunDataSource.postRun(
            pendingRunEntity.run.toRun(),
            pendingRunEntity.mapPictureBytes
        )) {
            is com.dsphoenix.core.domain.util.Result.Error -> result.error.toWorkerResult()
            is com.dsphoenix.core.domain.util.Result.Success -> {
                localRunDataSource.upsertRun(result.data)
                pendingSyncDao.deleteRunPendingSyncEntity(pendingRunId)
                return Result.success()
            }
        }
    }

    companion object {
        const val RUN_ID = "RUN_ID"
    }
}
