package com.example.mobappfrontend

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/*TODO: convert Json object to kotlin data class*/
//Separating and saving json files to dataclass
//FileHandling.kt done by Julian

//Oriented by: https://stackoverflow.com/questions/65875130/how-to-parse-a-json-file-in-kotlin-on-android-studio
fun converter(context: Context) {
    // opens the json file and safes it in input stream
    val inputStream = context.openFileInput("filtered_shows.json")
    // reads the json file and safes it as string
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    //decodefromstring is used to transfer string in our data class
    showContainer = Json { ignoreUnknownKeys = true }.decodeFromString<ShowContainer>(jsonString)

    // clear topiccardslist if it isnt empty
    if(topicCardsList != null) {
        topicCardsList.clear()
    }

    //every show object will be added to topicCardsList
    for (show in showContainer!!.items) {
        topicCardsList.add(Topiccard(show.title, show.webLink, show.imageUrl))
    }
}