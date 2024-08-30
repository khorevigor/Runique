package com.dsphoenix.run.network

data class RunDto(
    val id: String?,
    val dateTimeUtc: String,
    val durationMillis: Long,
    val distanceMeters: Int,
    val lat: Double,
    val long: Double,
    val avgSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String?,
    val avgHeartRate: Int?,
    val maxHeartRate: Int?
) {
    // No-argument constructor for Firestore
    constructor() : this(
        null,
        "",
        0,
        0,
        0.0,
        0.0,
        0.0,
        0.0,
        0,
        null,
        null,
        null
    )
}
