package com.example.mobappfrontend

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


//-----------------COLORS-----------------<<<<<<<<
//for dropdown spinner background
val customColor1 = Color.hsl(228F, 0.6F,0.6F,1f, ColorSpaces.Srgb)
//for middle section background
val customColor2 = Color.hsl(226F, 0.4F,0.7F,1f, ColorSpaces.Srgb)
//for ARD logo
val customColor3 = Color.hsl(180F, 0.6F,0.7F,1f, ColorSpaces.Srgb)

//-----------------Dropdown selectables-----------------<<<<<<<<
val cities = listOf(
    "Stuttgart", "München", "Berlin", "Potsdam", "Bremen", "Hamburg", "Wiesbaden", "Schwerin",
    "Hannover", "Düsseldorf", "Mainz", "Saarbrücken", "Dresden", "Magdeburg", "Kiel", "Erfurt"
)

//liste aus Json datensatz generieren und an datenklasse übergeben, diese datenklassen als liste an lazycolumn übergeben
var topicCardsList :MutableList<Topiccard> = mutableListOf()


//-----------------Parser-----------------<<<<<<<<
var showContainer: ShowContainer? = null

//-----------------Location-----------------<<<<<<<<
var locationCallback: LocationCallback? = null
var fusedLocationClient: FusedLocationProviderClient? = null
var locationRequired = false

val permissions = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
)

var currentLocationForPrint = LocationDetails(0.toDouble(), 0.toDouble())


//------------->>>------------->>> Main
class MainActivity : ComponentActivity() { //ComponentActivity() or AppCompatActivity() ??
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //val context = LocalContext.current

            Locationing(this)
            AppContent(this)


        }
    }

    override fun onResume() {
        super.onResume()
        if (locationRequired) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }
}


//TODO: Preview renderer
/*@Composable
@Preview
fun ThreeColumnScreenPreview() {
    AppContent()
}
*/

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

