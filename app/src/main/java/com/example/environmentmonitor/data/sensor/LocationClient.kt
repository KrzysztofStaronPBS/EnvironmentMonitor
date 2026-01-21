package com.example.environmentmonitor.data.sensor

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationClient @Inject constructor(
    private val context: Context,
    private val client: FusedLocationProviderClient
) {
    @SuppressLint("MissingPermission")
    fun getLocationUpdates(intervalMs: Long): Flow<Location> = callbackFlow {
        // sprawdzanie uprawnień powinno odbyć się w UI, tutaj zakładam ich obecność (albo błąd)

        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, intervalMs)
            .setMinUpdateIntervalMillis(intervalMs / 2)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.lastLocation?.let { location ->
                    launch { send(location) }
                }
            }
        }

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())

        // zamknięcie strumienia - bardzo ważne dla oszczędzania baterii
        awaitClose {
            client.removeLocationUpdates(callback)
        }
    }
}