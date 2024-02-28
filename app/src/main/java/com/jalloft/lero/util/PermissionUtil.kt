package com.jalloft.lero.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jalloft.lero.data.domain.GeoLocalization
import com.jalloft.lero.util.CommonUtil.findActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Created by Jardson Costa on 27/02/2024.
 */
object PermissionUtil {

    const val LOCALIZATION_PERMISSION_DANIED_COUNT = "localization_permission_danied_count"
    fun checkLocationPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


//    @SuppressLint("MissingPermission")
//    private fun getLastKnownLocation(context: Context): Location? {
//        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val providers = locationManager.getProviders(true)
//        var bestLocation: Location? = null
//        for (provider in providers) {
//            val location = locationManager.getLastKnownLocation(provider) ?: continue
//            if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
//                bestLocation = location
//            }
//        }
//        return bestLocation
//    }

    @SuppressLint("MissingPermission")
    suspend fun getLocation(context: Context): GeoLocalization {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        return withContext(Dispatchers.IO) {
            val localization: Location? = fusedLocationClient.lastLocation.await()
            localization?.let { GeoLocalization(it.latitude, it.longitude) } ?: GeoLocalization()
        }
    }

    fun requestPerimission(
        context: Context,
        onPermissionGranted: () -> Unit,
        onRequestPermission: () -> Unit,
        onShowRequestPermissionRationale: () -> Unit
    ) {
        when {
            checkLocationPermission(context) -> {
                onPermissionGranted()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                context.findActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                onShowRequestPermissionRationale()
            }

            else -> {
                onRequestPermission()
            }
        }
    }

}