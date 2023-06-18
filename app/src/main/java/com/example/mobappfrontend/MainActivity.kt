import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ComponentActivity


data class JsonObject(val id: Int, val data: String)

val cities = listOf(
    "City 1", "City 2", "City 3", "City 4", "City 5", "City 6", "City 7", "City 8",
    "City 9", "City 10", "City 11", "City 12", "City 13", "City 14", "City 15", "City 16"
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
@Preview(showBackground = true) //show preview

/*
@Composable
fun MainComposable() {
    Card(
        elevation = 5.dp,
        backgroundColor = Color.DarkGray,
        border = BorderStroke(2.dp, Color.Black),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(1f)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ){}
    }

}
*/

@Composable
fun AppContent() {
    val selectedCity = remember { mutableStateOf("") }
    val selectedLocation = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "Jetpack Compose App") },
            actions = {
                Button(
                    onClick = { selectedLocation.value = "User Location" },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(text = "Get Location")
                }
            }
        )

        Box(modifier = Modifier.padding(15.dp)) {
            MyUI()
            DropdownMenu(
                expanded = selectedCity.value.isNotEmpty(),
                onDismissRequest = { selectedCity.value = "" },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                cities.forEach { city ->
                    DropdownMenuItem(onClick = { selectedCity.value = city }) {
                        Text(text = city)
                    }
                }
            }
            Text(
                text = selectedCity.value.takeIf { it.isNotEmpty() } ?: "Select a city",
                modifier = Modifier.padding(8.dp)
            )
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(jsonData) { item ->
                Text(text = item.data)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyUI() {
    val listItems = arrayOf("Favorites", "Options", "Settings", "Share")
    val contextForToast = LocalContext.current.applicationContext

    // state of the menu
    var expanded by remember {
        mutableStateOf(false)
    }

    // remember the selected item
    var selectedItem by remember {
        mutableStateOf(listItems[0])
    }

    // box
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        // text field
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // this is a column scope
            // all the items are added vertically
            listItems.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(onClick = {
                    selectedItem = selectedOption
                    Toast.makeText(contextForToast, selectedOption, Toast.LENGTH_SHORT).show()
                    expanded = false
                }) {
                    Text(text = selectedOption)
                }
            }
        }
    }
}

