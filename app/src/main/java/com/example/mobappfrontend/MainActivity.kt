import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.Surface
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.colorspace.ColorSpaces
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

//---------------->>> Main
class MainActivity : androidx.activity.ComponentActivity() { //ComponentActivity() or AppCompatActivity() ??
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

@Preview
@Composable
fun ThreeColumnScreenPreview() {
    AppContent()
}
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

@Composable
fun TopSection() {
    Surface(color = Color.Blue) {
        Row(modifier = Modifier.fillMaxSize()) {
            //Dropdownmenu
            Column(modifier = Modifier.weight(0.1f).fillMaxHeight()) { }

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
            colors = ExposedDropdownMenuDefaults.textFieldColors(textColor = Color.White, disabledTextColor = Color.Gray, backgroundColor = customColor1, )
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

fun BErequestMan(dditem: String) {
    println("$dditem")
    //Here follows the backend request for manual selection
}






