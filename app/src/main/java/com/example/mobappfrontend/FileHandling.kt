package com.example.mobappfrontend

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/*TODO: convert Json object to kotlin data class*/
//Separating and saveing json files to dataclass
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