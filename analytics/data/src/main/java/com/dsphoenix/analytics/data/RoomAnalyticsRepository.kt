package com.dsphoenix.analytics.data

import com.dsphoenix.analytics.domain.AnalyticsRepository
import com.dsphoenix.analytics.domain.AnalyticsValues
import com.dsphoenix.core.database.dao.AnalyticsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RoomAnalyticsRepository(
    private val analyticsDao: AnalyticsDao
): AnalyticsRepository {

    override suspend fun getAnalyticsValues(): AnalyticsValues {
        return withContext(Dispatchers.IO) {
            val totalDistance = async { analyticsDao.getTotalDistance() }
            val totalTime = async { analyticsDao.getTotalTime() }
            val maxSpeed = async { analyticsDao.getMaxSpeed() }
            val avgDistance = async { analyticsDao.getAvgDistance() }
            val avgPace = async { analyticsDao.getAvgPace() }

            AnalyticsValues(
                totalDistanceMeters = totalDistance.await(),
                totalTimeMillis = totalTime.await().toDuration(DurationUnit.MILLISECONDS),
                maxSpeedKmh = maxSpeed.await(),
                avgDistanceMeters = avgDistance.await(),
                avgPaceSeconds = avgPace.await()
            )
        }
    }
}
