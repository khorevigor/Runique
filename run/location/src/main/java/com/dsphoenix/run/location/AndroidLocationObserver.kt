package com.dsphoenix.run.location

import android.content.Context
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.getSystemService
import com.dsphoenix.core.domain.location.LocationWithAltitude
import com.dsphoenix.run.domain.LocationObserver
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AndroidLocationObserver(
    private val context: Context
): LocationObserver {

    private val client = LocationServices.getFusedLocationProviderClient(context)

    override fun observeLocation(interval: Long): Flow<LocationWithAltitude> {
        return callbackFlow {
            val locationManager = context.getSystemService<LocationManager>()!!
            var isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            var isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            while(!isGpsEnabled && !isNetworkEnabled) {
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                delay(3000L)
            }

            client.lastLocation.addOnSuccessListener { it ->
                it?.let { location ->
                    trySend(location.toLocationWithAltitude())
                }
            }

            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result : LocationResult) {
                    super.onLocationResult(result)

                    result.locations.lastOrNull()?.let { location ->
                        trySend(location.toLocationWithAltitude())
                    }
                }
            }

            client.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }
}
