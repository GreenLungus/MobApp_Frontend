package com.example.mobappfrontend

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.request.get

//TODO: Backend request
//Connections.kt done by Julian

//dbitems is the city name and context will be needed to access data operation

// Oriented by: https://ktor.io/docs/request.html#http-method
// https://www.javatpoint.com/kotlin-android-read-and-write-internal-storage
// https://stackoverflow.com/questions/6760585/accessing-localhostport-from-android-emulator
suspend fun beRequestMan(context: Context, cityName: String) {
    // Creation of HTTP-Client
    val client = HttpClient {}

    val url = "http://10.0.2.2:8080/$cityName"
    try {
        //send request to backend
        val response: String = client.get(url)

        // opens "filtered_shows.json" from internal memory -> private is just for this application
        context.openFileOutput("filtered_shows.json", Context.MODE_PRIVATE).use {
            it.write(response.toByteArray())
        }
        //calls parse function to store data in our list
        converter(context)
    } catch(e: Exception) {
        // Prints error if request was not success
        e.printStackTrace()
    } finally {
        // close HTTP-Client whether its success or failed
        client.close()
    }
}

//context will be needed to access data operation, latitude and longitude are coordinates for request
suspend fun beRequestLocation(context: Context, latitude: Double?, longitude: Double?) {
    // Creation of HTTP-Client
    val client = HttpClient {}

    val url = "http://10.0.2.2:8080/getCityByCoordinates?latitude=$latitude&longitude=$longitude"

    try {
        //send request to backend
        val response: String = client.get(url)

        // opens "filtered_shows.json" from internal memory -> private is just for this application
        context.openFileOutput("filtered_shows.json", Context.MODE_PRIVATE).use {
            it.write(response.toByteArray())
        }
        //calls parse function to store data in our list
        converter(context)
    } catch(e: Exception) {
        // Prints error if request was not success
        e.printStackTrace()
    } finally {
        // close HTTP-Client whether its success or failed
        client.close()
    }
}