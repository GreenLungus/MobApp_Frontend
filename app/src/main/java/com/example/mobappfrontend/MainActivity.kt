import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
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
import androidx.core.content.ContextCompat.startActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


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


//------------->>>------------->>> Main
class MainActivity : ComponentActivity() { //ComponentActivity() or AppCompatActivity() ??
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
            //Text("Hello World")
            //MainComposable()
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
                //LazyColumnTopics()
                TopicCards()
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
                    .weight(0.8f)){ }
                Row(modifier = Modifier
                    .fillMaxSize()
                    .weight(2f)){ DropdownCitiesSelectable() }
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
                    .fillMaxHeight()
            ) {
                // Content for the third column
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
                    BErequestMan(selectedItem)
                }) {
                    Text(text = selectedOption) //text in dropdown rows for each city
                }
            }
        }
    }
}

//TODO: Content cards
@Composable
fun TopicCards() {
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
            modifier = Modifier.padding(10.dp)
        ) {
            //Image preview
            Text(text = "IMAGE\n  here", color = Color.White, fontSize = 15.sp)

            //URL and Title
            Column(

                modifier = Modifier.padding(10.dp)
            ) {
                var link = "https://www.google.com"
                Text(text = "This is Title ***************", color = Color.White, fontSize = 20.sp)
                OpenLinkButton(link)
            }
        }

    }
}

//TODO: Backend request
fun BErequestMan(dbitem: String) {
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



//TODO: scrollable list (infinite scroll ?)
//https://medium.com/@mal7othify/lists-using-lazycolumn-in-jetpack-compose-c70c39805fbc
@Composable
fun LazyColumnTopics() {
    val listt = listOf(
    "A", "B"
    ) + ((0..100).map { it.toString() })

    LazyColumn(modifier = Modifier.fillMaxHeight()) {

    }
}

//TODO: hyperlink button
@Composable
fun OpenLinkButton(link: String) {
    val context = LocalContext.current
    Box(
        //modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                context.startActivity(intent)
            }
        ) {
            Text(text = "Anschauen!")
        }
    }
}

