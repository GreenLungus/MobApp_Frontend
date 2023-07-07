package com.example.mobappfrontend

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices



@Composable
fun Locationing (context: Context) {

    var currentLocation by remember {
        mutableStateOf(LocationDetails(0.toDouble(), 0.toDouble()))
    }
    //getting locationdata to print for debug
    currentLocationForPrint = currentLocation

    // implemented as in documentation:
    //https://developer.android.com/training/location/retrieve-current#kotlin
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    //https://developer.android.com/training/location/request-updates
    locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                // Update UI with location data
                currentLocation = LocationDetails(location.latitude, location.longitude)
            }
        }
    }

}

@Composable
fun LocationButton(context: Context) {

    //Call Androids PermissionMap and Toasters for Access information
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            locationRequired = true
            startLocationUpdates()
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    //Button with Location Icon
    FilledTonalIconButton(
        shape = RoundedCornerShape(15.dp),
        onClick = {
            if (permissions.all {
                    ContextCompat.checkSelfPermission(
                        context,
                        it
                    ) == PackageManager.PERMISSION_GRANTED //permission from Popup window
                }) {
                // if access granted: Get the location
                startLocationUpdates()
            }
            else {
                launcherMultiplePermissions.launch(permissions)
            }

            //debug purpose
            println("Latitude : " + currentLocationForPrint.latitude)
            println("Longitude : " + currentLocationForPrint.longitude)
        })
    {
        Icon(
            Icons.Outlined.LocationOn,
            contentDescription = "Location determination"
        )
    }
}


@SuppressLint("MissingPermission")
fun startLocationUpdates() {
    locationCallback?.let {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        //https://developer.android.com/training/location/request-updates
        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
}