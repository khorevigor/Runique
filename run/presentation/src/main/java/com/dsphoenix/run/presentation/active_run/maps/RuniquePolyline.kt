package com.dsphoenix.run.presentation.active_run.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.dsphoenix.core.domain.location.LocationTimestamp
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline

@Composable
fun RuniquePolylines(locations: List<List<LocationTimestamp>>) {
    val polylines = remember(locations) {
        locations.map {
            it.zipWithNext { timestamp1, timestamp2 ->
                PolylineUi(
                    location1 = timestamp1.location.location,
                    location2 = timestamp2.location.location,
                    color = PolylineColorCalculator.locationsToColor(timestamp1, timestamp2)
                )
            }
        }
    }

    polylines.forEach {
        it.forEach { polylineUi ->
            Polyline(
                points = listOf(
                    LatLng(polylineUi.location1.lat, polylineUi.location1.long),
                    LatLng(polylineUi.location2.lat, polylineUi.location2.long)
                ),
                color = polylineUi.color,
                jointType = JointType.BEVEL
            )
        }
    }
}
