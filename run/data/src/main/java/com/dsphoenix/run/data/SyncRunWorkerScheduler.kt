package com.dsphoenix.run.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.dsphoenix.core.database.dao.RunPendingSyncDao
import com.dsphoenix.core.database.entity.DeletedRunSyncEntity
import com.dsphoenix.core.database.entity.RunPendingSyncEntity
import com.dsphoenix.core.database.mappers.toRunEntity
import com.dsphoenix.core.domain.auth.AuthRepository
import com.dsphoenix.core.domain.run.Run
import com.dsphoenix.core.domain.run.RunId
import com.dsphoenix.core.domain.run.SyncRunScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncRunWorkerScheduler(
    context: Context,
    private val pendingSyncDao: RunPendingSyncDao,
    private val authRepository: AuthRepository,
    private val applicationScope: CoroutineScope
) : SyncRunScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun cancelAllSyncs() {
        workManager
            .cancelAllWork()
            .await()
    }

    override suspend fun scheduleSync(syncType: SyncRunScheduler.SyncType) {
        when (syncType) {
            is SyncRunScheduler.SyncType.FetchRuns -> scheduleFetchRunsWorker(syncType.interval)
            is SyncRunScheduler.SyncType.CreateRun -> scheduleCreateRunWorker(syncType.run, syncType.mapPictureBytes)
            is SyncRunScheduler.SyncType.DeleteRun -> scheduleDeleteRunWorker(syncType.runId)
        }
    }

    private suspend fun scheduleFetchRunsWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager
                .getWorkInfosByTag(FETCH_RUNS_WORK_TAG)
                .get()
                .isNotEmpty()
        }

        if (isSyncScheduled)
            return

        val workRequest = PeriodicWorkRequestBuilder<FetchRunsWorker>(
            repeatInterval = interval.toJavaDuration()
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInitialDelay(
                duration = 30,
                timeUnit = TimeUnit.MINUTES
            )
            .addTag(FETCH_RUNS_WORK_TAG)
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }
    }

    private suspend fun scheduleCreateRunWorker(run: Run, mapPictureBytes: ByteArray) {
        val userId = authRepository.getUserId()

        val pendingRun = RunPendingSyncEntity(
            run = run.toRunEntity(),
            mapPictureBytes = mapPictureBytes,
            userId = userId
        )

        pendingSyncDao.upsertRunPendingSyncEntity(pendingRun)

        val workRequest = OneTimeWorkRequestBuilder<CreateRunWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(CreateRunWorker.RUN_ID, pendingRun.runId)
                    .build()
            )
            .addTag(CREATE_RUN_WORK_TAG)
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }
    }

    private suspend fun scheduleDeleteRunWorker(runId: RunId) {
        val userId = authRepository.getUserId()

        val entity = DeletedRunSyncEntity(runId, userId)
        pendingSyncDao.upsertDeletedRunSyncEntity(entity)

        val workRequest = OneTimeWorkRequestBuilder<DeleteRunWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(DeleteRunWorker.RUN_ID, runId)
                    .build()
            )
            .addTag(DELETE_RUN_WORK_TAG)
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }
    }

    companion object {
        const val FETCH_RUNS_WORK_TAG = "fetch_runs_work_tag"
        const val CREATE_RUN_WORK_TAG = "create_run_work_tag"
        const val DELETE_RUN_WORK_TAG = "delete_run_work_tag"
    }

}
