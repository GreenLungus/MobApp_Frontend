package com.example.mobappfrontend

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

//TODO: Backend request
suspend fun beRequestMan(context: Context, dbitem: String) {
    // Creation of HTTP-Client
    val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    // Sending GET-Request to Backend-Server
    val url = "http://10.0.2.2:8080/$dbitem"
    val response: String = client.get(url) //TODO: catch crshing with try and catch

    // Writes response to json file and fills dataclass
    context.openFileOutput("filtered_shows.json", Context.MODE_PRIVATE).use {
        it.write(response.toByteArray())
    }
    parse(context)

    // close HTTP-Client
    client.close()
}

