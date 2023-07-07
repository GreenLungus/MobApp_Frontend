package com.example.mobappfrontend

import kotlinx.serialization.Serializable

//for topiccards content
data class Topiccard(
    val title: String,
    val url: String,
    val img: String
)

//containers for data after separating json file
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

//for storing location
data class LocationDetails(
    val latitude: Double,
    val longitude: Double
)