package com.dsphoenix.core.database

import android.database.sqlite.SQLiteFullException
import com.dsphoenix.core.database.dao.RunDao
import com.dsphoenix.core.database.mappers.toRun
import com.dsphoenix.core.database.mappers.toRunEntity
import com.dsphoenix.core.domain.run.LocalRunDataSource
import com.dsphoenix.core.domain.run.Run
import com.dsphoenix.core.domain.run.RunId
import com.dsphoenix.core.domain.util.DataError
import com.dsphoenix.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalRunDataSource(
    private val runDao: RunDao
): LocalRunDataSource {

    override fun getRuns(): Flow<List<Run>> {
        return runDao.getRuns().map { entities ->
            entities.map {
                it.toRun()
            }
        }
    }

    override suspend fun upsertRun(run: Run): Result<RunId, DataError.Local> {
        return try {
            val entity = run.toRunEntity()
            runDao.upsertRun(entity)
            Result.Success(entity.id)
        } catch (e: SQLiteFullException) {
            return Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, DataError.Local> {
        return try {
            val entities = runs.map { it.toRunEntity() }
            val ids = entities.map { it.id }
            runDao.upsertRuns(entities)
            Result.Success(ids)
        } catch (e: SQLiteFullException) {
            return Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteRun(id: String) {
        runDao.deleteRun(id)
    }

    override suspend fun deleteAllRuns() {
        runDao.deleteAllRuns()
    }
}
