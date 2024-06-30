package com.dsphoenix.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dsphoenix.core.database.dao.RunDao
import com.dsphoenix.core.database.dao.RunPendingSyncDao
import com.dsphoenix.core.database.entity.DeletedRunSyncEntity
import com.dsphoenix.core.database.entity.RunEntity
import com.dsphoenix.core.database.entity.RunPendingSyncEntity

@Database(
    entities = [
        RunEntity::class,
        RunPendingSyncEntity::class,
        DeletedRunSyncEntity::class
    ],
    version = 1
)
abstract class RunDatabase : RoomDatabase() {

    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
}
