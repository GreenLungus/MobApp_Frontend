package com.example.mobappfrontend

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback



//-----------------fast access custom COLORS-----------------<<<<<<<<
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


//-----------------List of data objects for Topiccards-----------------<<<<<<<<
var topicCardsList :MutableList<Topiccard> = mutableListOf()


//-----------------Container: Data Objects generated out of Json file-----------------<<<<<<<<
var showContainer: ShowContainer? = null


//-----------------Location-----------------<<<<<<<<
var locationCallback: LocationCallback? = null
var fusedLocationClient: FusedLocationProviderClient? = null
var locationRequired = false


// array of all needed permissions to check with
val permissions = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
)

var currentLocationForPrint = LocationDetails(0.toDouble(), 0.toDouble())


//------------->>>------------->>> Main
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

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

