package com.example.mobappfrontend

import android.content.Context
import android.content.Intent
//import android.media.Image
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.core.content.ContextCompat.startActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
//import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


data class JsonObject(val id: Int, val data: String)

//for dropdown spinner background
val customColor1 = Color.hsl(228F, 0.6F,0.6F,1f, ColorSpaces.Srgb)
//for middle section background
val customColor2 = Color.hsl(226F, 0.4F,0.7F,1f, ColorSpaces.Srgb)

val cities = listOf(
    "Stuttgart", "München", "Berlin", "Potsdam", "Bremen", "Hamburg", "Wiesbaden", "Schwerin",
    "Hannover", "Düsseldorf", "Mainz", "Saarbrücken", "Dresden", "Magdeburg", "Kiel", "Erfurt"
)

val jsonData = mutableStateListOf(
    JsonObject(1, "Data 1"),
    JsonObject(2, "Data 2"),
    JsonObject(3, "Data 3"),
    // ... add more JSON objects as needed
)

var showContainer: ShowContainer? = null;
//------------->>>------------->>> Main
class MainActivity : ComponentActivity() { //ComponentActivity() or AppCompatActivity() ??
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            /*
            parse(this)
            if (showContainer != null) {
                for (show in showContainer!!.items) {
                    println("Title: ${show.title}")
                    println("Web link: ${show.links.web}")
                    /*for (image in show.images) {
                            println("Image URL: ${image.url}")
                        }*/
                }
            }*/
            AppContent()
        }
    }
}

//---------------->>> Composables
//@Preview(showBackground = true) //show preview

//TODO: Preview renderer
@Preview
@Composable
fun ThreeColumnScreenPreview() {
    AppContent()
}

//TODO: base app layout
@Composable
fun AppContent() {
    Surface(color = customColor2/*MaterialTheme.colors.background*/) {
        Column(modifier = Modifier.fillMaxSize()) {
            //Topsection
            Row(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxWidth()
            ) {
                // Content for the first row
                TopSection()
            }
            //Middlesection
            Row(
                modifier = Modifier
                    .weight(5f)
                    .fillMaxWidth()
            ) {
                // Content for the second row
                LazyColumnTopics()
                //TopicCards()//liste ubergeben mit geparstem json
            }
            //Bottomsection
            Row(

                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth()
                    .background(Color.Blue)
                    //.padding(40.dp)
                    //.clip(shape = RoundedCornerShape(20.dp))

            ) {
                // Content for the third row
            }
        }
    }
}

//TODO: Header
@Composable
fun TopSection() {
    Surface(color = Color.Blue) {
        Row(modifier = Modifier.fillMaxSize()) {
            //Dropdownmenu
            Column(modifier = Modifier
                .weight(0.1f)
                .fillMaxHeight()) { }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
            ) {
                // Content for the first column
                Row(modifier = Modifier
                    .fillMaxSize()
                    .weight(0.5f)){ }
                Row(modifier = Modifier
                    .fillMaxSize()
                    .weight(2.3f)){ DropdownCitiesSelectable() }
                //Row(modifier = Modifier.fillMaxSize().weight(1f)){ }
            }
            //Button for Dropdownmenu Request submit
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // Content for the second column
            }
            //Button for auto location
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                // Content for the third column
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    FilledTonalIconButton(
                        shape = RoundedCornerShape(15.dp),
                        onClick = { /*TODO: get location, send to backend*/ })
                    {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = "Location determination"
                        )
                    }
                }
            }
        }
    }
}

//TODO: Dropdownmenu
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropdownCitiesSelectable() {
    // state of the menu - either expanded or not - default false
    var expandstate by remember {
        mutableStateOf(false)
    }

    // remember the selected item and use first in list as default
    var selectedItem by remember {
        mutableStateOf(cities[0])
    }

    // box
    ExposedDropdownMenuBox(
        modifier = Modifier
            .clip(RoundedCornerShape(13.dp)),
        expanded = expandstate,
        onExpandedChange = {
            expandstate  = !expandstate
        }

    ) {
        // text field
        TextField(
            value = selectedItem, //display selected item, later IMPORTANT for backend communication
            onValueChange = {},    //left empty
            readOnly = true,
            label = { Text(text = "Stadt auswählen:") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expandstate
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                textColor = Color.White,
                disabledTextColor = Color.Gray,
                backgroundColor = customColor1
            )
        )

        // menu
        ExposedDropdownMenu(
            expanded = expandstate,
            onDismissRequest = { expandstate = false }
        ) {
            // all the items are added vertically
            cities.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(onClick = {
                    //on click Dropdown
                    selectedItem = selectedOption
                    expandstate = false
                    beRequestMan(selectedItem) // <<---- BE Request with selected item, could be get erlaier as default
                }) {
                    Text(text = selectedOption) //text in dropdown rows for each city
                }
            }
        }
    }
}

//TODO: Content cards
@Composable
fun TopicCards(topic: Topiccard) {
    //should get the title, url and img source in single form or as json obj
    Card(
        elevation = 5.dp,
        backgroundColor = customColor1,
        border = BorderStroke(2.dp, Color.White),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(1f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(5.dp)
        ) {
            //Image preview
            //Text(text = "IMAGE\n  here", color = Color.White, fontSize = 15.sp)

            val image: Painter = rememberAsyncImagePainter(topic.img)
            Image(
                modifier = Modifier
                    .size(80.dp, 80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                painter = image,
                alignment = Alignment.CenterStart,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            //URL and Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(5.dp)
                    .weight(2f)
            ) {
                //var link = topic.url
                //Text(text = "This is Title ***************", color = Color.White, fontSize = 20.sp)

                Text(
                    text = topic.title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 17.sp
                )
                OpenLinkButton(link = topic.url)
            }
        }

    }
}

//TODO: Backend request
fun beRequestMan(dbitem: String) {
    println("$dbitem")
    //Here follows the backend request for manual selection
}

//Kotlin data class that represents the structure of the JSON object.
//Make sure the property names in the Kotlin class match the keys in the JSON object.
//1. get object and read into an string
//2. count rows? words? to determine where the index is and an object starts
//3. deserialize using gson and write into datac lass and add data class object to list
//4. use data class list to fill list on main screen
//TODO: convert Json object to kotlin data class
data class Topiccard(
    val title: String,
    val url: String,
    val img: String
)
@Serializable
data class Image(
    val url: String
)

@Serializable
data class Show(
    val title: String,
    val links: Links,
    val images: List<Image>,
    val keywords: List<String>
)

@Serializable
data class Links(
    val web: String
)

@Serializable
data class ShowContainer(
    val items: List<Show>
)

// Parse through the json file and safe shows in global
fun parse(context: Context) {
    val inputStream = context.resources.openRawResource(R.raw.filtered_shows)
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    showContainer = Json { ignoreUnknownKeys = true }.decodeFromString<ShowContainer>(jsonString)
}


//TODO: scrollable list (infinite scroll ?)
//https://medium.com/@mal7othify/lists-using-lazycolumn-in-jetpack-compose-c70c39805fbc
@Composable
fun LazyColumnTopics() {
    LazyColumn {
        //liste aus Json datensatz generieren und an datenklasse übergeben, diese datenklassen als liste an lazycolumn übergeben
        val topicCardsList :List<Topiccard> = listOf(
            Topiccard("So sicher wie Fort Knox (128)", "https://www.ardmediathek.de/video/ODFlZmExYzUtZWJlMy00YTA2LWFlOTQtNTU3MTg1ZGRiODVk", "https://api.ardmediathek.de/image-service/image-collections/urn:ard:image-collection:1349ca4a85ad8334/16x9?imwidth=1920&w=1920"),
            Topiccard("Tatort: Hinter dem Spiegel", "https://www.ardmediathek.de/video/Y3JpZDovL3N3ci5kZS9hZXgvbzE4MDA0MzU", "https://api.ardmediathek.de/image-service/image-collections/urn:ard:image-collection:a29af08618b987d8/16x9?imwidth=1920&w=1920"),
            Topiccard("Das letzte Rennen", "https://www.ardmediathek.de/video/Y3JpZDovL3N3ci5kZS9hZXgvbzE3NDYzODU", "https://api.ardmediathek.de/image-service/image-collections/urn:ard:image-collection:a8ce3a563082adae/16x9?imwidth=1920&w=1920"),
            Topiccard("So sicher wie Fort Knox (128)", "https://www.ardmediathek.de/video/ODFlZmExYzUtZWJlMy00YTA2LWFlOTQtNTU3MTg1ZGRiODVk", "https://api.ardmediathek.de/image-service/image-collections/urn:ard:image-collection:1349ca4a85ad8334/16x9?imwidth=1920&w=1920"),
            Topiccard("Tatort: Hinter dem Spiegel", "https://www.ardmediathek.de/video/Y3JpZDovL3N3ci5kZS9hZXgvbzE4MDA0MzU", "https://api.ardmediathek.de/image-service/image-collections/urn:ard:image-collection:a29af08618b987d8/16x9?imwidth=1920&w=1920"),
            Topiccard("Das letzte Rennen", "https://www.ardmediathek.de/video/Y3JpZDovL3N3ci5kZS9hZXgvbzE3NDYzODU", "https://api.ardmediathek.de/image-service/image-collections/urn:ard:image-collection:a8ce3a563082adae/16x9?imwidth=1920&w=1920"),
            Topiccard("So sicher wie Fort Knox (128)", "https://www.ardmediathek.de/video/ODFlZmExYzUtZWJlMy00YTA2LWFlOTQtNTU3MTg1ZGRiODVk", "https://api.ardmediathek.de/image-service/image-collections/urn:ard:image-collection:1349ca4a85ad8334/16x9?imwidth=1920&w=1920"),
            Topiccard("Tatort: Hinter dem Spiegel", "https://www.ardmediathek.de/video/Y3JpZDovL3N3ci5kZS9hZXgvbzE4MDA0MzU", "https://api.ardmediathek.de/image-service/image-collections/urn:ard:image-collection:a29af08618b987d8/16x9?imwidth=1920&w=1920"),
            Topiccard("Das letzte Rennen", "https://www.ardmediathek.de/video/Y3JpZDovL3N3ci5kZS9hZXgvbzE3NDYzODU", "https://api.ardmediathek.de/image-service/image-collections/urn:ard:image-collection:a8ce3a563082adae/16x9?imwidth=1920&w=1920"),
            Topiccard("So sicher wie Fort Knox (128)", "https://www.ardmediathek.de/video/ODFlZmExYzUtZWJlMy00YTA2LWFlOTQtNTU3MTg1ZGRiODVk", "https://api.ardmediathek.de/image-service/image-collections/urn:ard:image-collection:1349ca4a85ad8334/16x9?imwidth=1920&w=1920"),
            Topiccard("Tatort: Hinter dem Spiegel", "https://www.ardmediathek.de/video/Y3JpZDovL3N3ci5kZS9hZXgvbzE4MDA0MzU", "https://api.ardmediathek.de/image-service/image-collections/urn:ard:image-collection:a29af08618b987d8/16x9?imwidth=1920&w=1920"),
            Topiccard("Das letzte Rennen", "https://www.ardmediathek.de/video/Y3JpZDovL3N3ci5kZS9hZXgvbzE3NDYzODU", "https://api.ardmediathek.de/image-service/image-collections/urn:ard:image-collection:a8ce3a563082adae/16x9?imwidth=1920&w=1920")
        )
        items(topicCardsList) {
            TopicCards(topic = it)
        }
    }
}

//TODO: hyperlink button
@Composable
fun OpenLinkButton(link: String) {
    val context = LocalContext.current
    Button(
        shape = RoundedCornerShape(15.dp),
        onClick = {
            //öffnet systembrowser mittels übergebener URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(intent)
        }
    ) {
            Text(text = " Jetzt anschauen!")
    }
}

