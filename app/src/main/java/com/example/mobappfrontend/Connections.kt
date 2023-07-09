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
    val response: String = client.get(url) //TODO: catch crashing with try and catch

    // Writes response to json file and fills dataclass
    context.openFileOutput("filtered_shows.json", Context.MODE_PRIVATE).use {
        it.write(response.toByteArray())
    }
    parse(context)

    // close HTTP-Client
    client.close()
}

//Try catch block didnt resolve crashing problem...
/*
suspend fun beRequestMan(context: Context, dbitem: String): List<ClipData.Item>{

        try {
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
            lateinit var response: String //TODO: catch crashing with try and catch
            //val response: String = client.get(url)
            response = client.get(url)
            // Writes response to json file and fills dataclass
            context.openFileOutput("filtered_shows.json", Context.MODE_PRIVATE).use {
                it.write(response.toByteArray())
            }
            parse(context)

            // close HTTP-Client
            client.close()
            return emptyList()


        } catch (e: RedirectResponseException){
            Toast.makeText(context, "Server Fehler", Toast.LENGTH_LONG).show()
            return emptyList()
        } catch (e: ClientRequestException){
            Toast.makeText(context, "Server Fehler", Toast.LENGTH_LONG).show()
            return emptyList()
        } catch (e: ServerResponseException){
            Toast.makeText(context, "Server Fehler", Toast.LENGTH_LONG).show()
            return emptyList()
        } catch (e: Exception){
            Toast.makeText(context, "Server Fehler", Toast.LENGTH_LONG).show()
            return emptyList()
        }
}
*/
