package com.example.mobappfrontend

import android.content.ClipData
import android.content.Context
import android.widget.Toast
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

//TODO: Backend request
//Class Connections.kt done by Julian
suspend fun beRequestMan(context: Context, dbitem: String) {
    // Creation of HTTP-Client
    val client = HttpClient {
        install(JsonFeature) { }
    }

    // Sending GET-Request to Backend-Server
    val url = "http://10.0.2.2:8080/$dbitem"
    try {
        val response: String = client.get(url)

        // Writes response to json file and fills dataclass
        context.openFileOutput("filtered_shows.json", Context.MODE_PRIVATE).use {
            it.write(response.toByteArray())
        }
        parse(context)
    } catch(e: Exception) {
        // Prints error if request was not success
        e.printStackTrace()
    } finally {
        // close HTTP-Client
        client.close()
    }
}

suspend fun beRequestLocation(context: Context, latitude: Double?, longitude: Double?) {
    val client = HttpClient {
        install(JsonFeature) { }
    }

    val url = "http://10.0.2.2:8080/getCityByCoordinates?latitude=$latitude&longitude=$longitude"

    try {
        val response: String = client.get(url)

        // Writes response to json file and fills dataclass
        context.openFileOutput("filtered_shows.json", Context.MODE_PRIVATE).use {
            it.write(response.toByteArray())
        }
        parse(context)
    } catch(e: Exception) {
        // Prints error if request was not success
        e.printStackTrace()
    } finally {
        // close HTTP-Client
        client.close()
    }
}