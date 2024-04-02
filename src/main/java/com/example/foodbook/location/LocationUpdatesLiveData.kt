package com.example.foodbook.location

import android.content.Context
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LocationUpdatesLiveData(context:Context) : LiveData<String>() {

    private val locationClient: FusedLocationProviderClient
            = LocationServices.getFusedLocationProviderClient(context)

    private val geocoder by lazy {
        Geocoder(context)
    }

    private val job = Job()

    private val scope = CoroutineScope(job + Dispatchers.IO)

    private val locationRequest = LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(20)
        priority = Priority.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            p0.lastLocation?.let {
                //postValue("${it.latitude},${it.longitude}")

                scope.launch {
                    val addresses = geocoder.getFromLocation(it.latitude,
                        it.longitude,1)
                    postValue(addresses!![0].getAddressLine(0))
                }

            }
        }
    }

    override fun onActive() {
        super.onActive()
        try {
            locationClient.requestLocationUpdates(
                locationRequest, locationCallback,
                Looper.getMainLooper()
            )
        }catch (e : SecurityException) {
            Log.d("LocationUpdatesLiveData","Missing location permission")
        }

    }


    override fun onInactive() {
        super.onInactive()
        job.cancel()
        locationClient.removeLocationUpdates(locationCallback)
    }
}