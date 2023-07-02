package com.example.mobappfrontend

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking


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

//-----------------LazyColumn Dataclass-----------------<<<<<<<<
data class Topiccard(
    val title: String,
    val url: String,
    val img: String
)

//liste aus Json datensatz generieren und an datenklasse übergeben, diese datenklassen als liste an lazycolumn übergeben
var topicCardsList :MutableList<Topiccard> = mutableListOf()


//-----------------Parser Dataclasses-----------------<<<<<<<<
var showContainer: ShowContainer? = null

@Serializable
data class Show(
    val title: String,
    val webLink: String,
    val imageUrl: String,
)

@Serializable
data class ShowContainer(
    val items: List<Show>
)

//------------->>>------------->>> Main
class MainActivity : ComponentActivity() { //ComponentActivity() or AppCompatActivity() ??
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent(this)
        }
    }
}

//---------------->>> Composables
//TODO: Preview renderer
/*@Preview
@Composable
fun ThreeColumnScreenPreview() {
    AppContent(this)
}*/

//TODO: base app layout
@Composable
fun AppContent(context: Context) {
    Surface(color = customColor2/*MaterialTheme.colors.background*/) {
        Column(modifier = Modifier.fillMaxSize()) {
            //Topsection
            Row(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxWidth()
            ) {
                // Content for the first row
                TopSection(context)
            }
            //Middlesection
            Row(
                modifier = Modifier
                    .weight(5f)
                    .fillMaxWidth()
            ) {
                // Content for the second row
                LazyColumnTopics()
            }
            //Bottomsection
            Row(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth()
                    .background(Color.Blue),
                horizontalArrangement = Arrangement.Center
            ) {
                // Content for the third row
                Box(
                    modifier = Modifier
                ) {
                    val image = painterResource(id = R.drawable.ard_logo)
                    Image(
                        painter = image,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = customColor3)
                    )
                }
            }
        }
    }
}

//TODO: Header
@Composable
fun TopSection(context: Context) {
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
                    .weight(2.3f)){ DropdownCitiesSelectable(context) }
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
fun DropdownCitiesSelectable(context: Context) {
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
                    runBlocking {
                        beRequestMan(context, selectedItem) // <<---- BE Request with selected item, could be get erlaier as default
                    }
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
            val image: Painter = rememberAsyncImagePainter(topic.img)
            Image(
                modifier = Modifier
                    .size(95.dp, 100.dp)
                    .clip(RoundedCornerShape(10.dp)),
                painter = image,
                alignment = Alignment.CenterStart,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            //URL and Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    //.padding(5.dp)
                    .weight(2f)
            ) {

                Text(
                    text = topic.title,
                    textAlign = TextAlign.Center,
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
suspend fun beRequestMan(context: Context, dbitem: String) {
    // Erstellen Sie einen neuen HTTP-Client
    val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    // Senden Sie eine GET-Anfrage an den Backend-Server
    val url = "http://10.0.2.2:8080/$dbitem"
    val response: String = client.get(url)

    // Schreibt die Antwort in eine JSON-Datei
    context.openFileOutput("filtered_shows.json", Context.MODE_PRIVATE).use {
        it.write(response.toByteArray())
    }
    parse(context)

    // Schließen Sie den HTTP-Client
    client.close()
}


/*TODO: convert Json object to kotlin data class*/
//Parse through the json file and safe shows in global
fun parse(context: Context) {
    val inputStream = context.openFileInput("filtered_shows.json")
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    showContainer = Json { ignoreUnknownKeys = true }.decodeFromString<ShowContainer>(jsonString)

    if(topicCardsList != null) {
        topicCardsList.clear()
    }

    for (show in showContainer!!.items) {
        println("Title: ${show.title}")
        println("Web link: ${show.webLink}")
        topicCardsList.add(Topiccard(show.title, show.webLink, show.imageUrl))
    }
}


//TODO: scrollable list (infinite scroll ?)
//https://medium.com/@mal7othify/lists-using-lazycolumn-in-jetpack-compose-c70c39805fbc
@Composable
fun LazyColumnTopics() {
    topicCardsList = remember { mutableStateListOf<Topiccard>() }
    LazyColumn {
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
