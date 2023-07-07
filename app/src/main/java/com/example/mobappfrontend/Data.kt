package com.example.mobappfrontend

import kotlinx.serialization.Serializable

data class Topiccard(
    val title: String,
    val url: String,
    val img: String
)

@Serializable
data class Show(
    val title: String,
    val webLink: String,
    val imageUrl: String,
)

@Serializable
data class ShowContainer(
    val items: List<Show>
)

data class LocationDetails(
    val latitude: Double,
    val longitude: Double
)