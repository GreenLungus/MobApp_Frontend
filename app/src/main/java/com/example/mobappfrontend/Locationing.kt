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
import kotlinx.coroutines.runBlocking
import android.Manifest

//setting up the location service by getting the Client
//setting up callback to be invoked when new location is computed
@Composable
fun Locationing (context: Context) {

    var currentLocation by remember {
        mutableStateOf(LocationDetails(null, null ))
    }
    //getting location data to print for debug and to use across application
    currentLocationForPrint = currentLocation

    // implemented as in documentation:
    //https://developer.android.com/training/location/retrieve-current#kotlin
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    //https://developer.android.com/training/location/request-updates
    locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                // Update with location data
                println("Location: " + location.latitude + " and " + location.longitude)
                currentLocation = LocationDetails(location.latitude, location.longitude)
            }
        }
    }

}

//location button with functionality
//permissions (AndroidManifest.xml) are asked for and rechecked by re-clicking
//location update and backendrequest is started when button clicked and permission is granted
@Composable
fun LocationButton(context: Context) {

    //Call Androids PermissionPopup and Toasters for Access information
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){ permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                Toast.makeText(context, "Standort freigegeben", Toast.LENGTH_LONG).show()
                locationRequired = true
                startLocationUpdates()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                Toast.makeText(context, "Präziser Standort benötigt", Toast.LENGTH_LONG).show()
            } else -> {
            // No location access granted.
            Toast.makeText(context, "Standort nicht freigegeben", Toast.LENGTH_LONG).show()
        }
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
                runBlocking {
                    beRequestLocation(context, currentLocationForPrint.latitude, currentLocationForPrint.longitude) //Backend Request for Location
                }
                //Errormessage if location is null
                //https://stackoverflow.com/questions/72168842/location-always-returning-null-all-the-time
                if(currentLocationForPrint.latitude == null && currentLocationForPrint.longitude == null ) {
                    Toast.makeText(context, "Etwas ist schief gelaufen!", Toast.LENGTH_LONG).show()
                }
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


//periodically updating the computed location data when service is running.
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