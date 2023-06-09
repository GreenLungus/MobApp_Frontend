package com.example.mobappfrontend

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.runBlocking


//base layout structured in top-section, middle-section and bottom-section
//TODO: base app layout
@Composable
fun AppContent(context: Context) {
    Surface(color = customColor2) {
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
                    //thus the application is not for publishing, the ard logo was used as an example for the look of the application.
                    //there is no interrelation between ARD and this application. Copyright by ARD
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

//Content structure of the top-section including spacers, dd menu and the location button
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
                    .weight(2.3f)){ DropdownCitiesSelectable(context) } //holding and calling the Dropdownmenu
            }
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
                    LocationButton(context) // holding and calling the location button
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
    ExposedDropdownMenuBox( //get the expandstate
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
            onValueChange = {},
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
                    //on click Dropdown (for each city element in DD Menu)
                    selectedItem = selectedOption
                    expandstate = false
                    //coroutine for suspend fun beRequest
                    runBlocking {
                        beRequestMan(context, selectedItem) // <<---- BE Request with selected city
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

            //topic image, used Coil to fetch image
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

            //Title and Button with URL
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
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

//TODO: scrollable list (infinite scroll ?)
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
            //opens system browser with var link: String(url)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(intent)
        }
    ) {
        Text(text = " Jetzt anschauen!")
    }
}

