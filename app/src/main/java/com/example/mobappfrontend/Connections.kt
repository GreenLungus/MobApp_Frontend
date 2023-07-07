package com.example.mobappfrontend

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

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