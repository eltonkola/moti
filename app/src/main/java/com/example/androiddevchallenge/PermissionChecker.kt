/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.runtime.compositionLocalOf
import androidx.core.content.ContextCompat
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.dsl.extension.requestPermissions
import com.example.androiddevchallenge.LocationRequest.LocationError
import com.example.androiddevchallenge.LocationRequest.LocationResponse
import com.example.androiddevchallenge.LocationRequest.PermissionDenied
import com.example.androiddevchallenge.LocationRequest.PermissionDeniedPermanently
import com.example.androiddevchallenge.LocationRequest.ShowRational
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

val PermissionHelper = compositionLocalOf<PermissionChecker> { error("No permission checker found!") }

sealed class LocationRequest {
    class LocationResponse(val lat: Double, val lon: Double) : LocationRequest()
    object LocationError : LocationRequest()
    object PermissionDenied : LocationRequest()
    object PermissionDeniedPermanently : LocationRequest()
    object ShowRational : LocationRequest()
}

class PermissionChecker(private val activity: MainActivity) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

    fun finish() {
        activity.finish()
    }

    fun listenForLocation(onAccepted: (LocationRequest) -> Unit) {
        if (activity.isFinishing) {
            return
        }
        if (ContextCompat.checkSelfPermission(activity, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(activity, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation(onAccepted)
            return
        }
        activity.requestPermissions(permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION) {
            requestCode = 4
            resultCallback = {
                when (this) {
                    is PermissionResult.PermissionGranted -> {
                        getLocation(onAccepted)
                    }
                    is PermissionResult.PermissionDenied -> {
                        onAccepted(PermissionDenied)
                    }
                    is PermissionResult.PermissionDeniedPermanently -> {
                        onAccepted(PermissionDeniedPermanently)
                    }
                    is PermissionResult.ShowRational -> {
                        onAccepted(ShowRational)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(onAccepted: (LocationRequest) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            Log.v("PermissionChecker", "location $location")
            location?.let {
                onAccepted(LocationResponse(it.latitude, it.longitude))
            } ?: run {
                listenForLocationOnce(onAccepted)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun listenForLocationOnce(onAccepted: (LocationRequest) -> Unit) {
        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 20 * 1000
            fastestInterval = 2 * 1000
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations.isNotEmpty()) {
                    val newLocation = locationResult.locations[0]
                    onAccepted(LocationResponse(newLocation.latitude, newLocation.longitude))
                } else {
                    onAccepted(LocationError)
                }
                fusedLocationClient.removeLocationUpdates(this)
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, activity.mainLooper)
    }
}
