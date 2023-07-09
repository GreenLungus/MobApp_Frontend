package com.example.mobappfrontend

import kotlinx.serialization.Serializable

//for topiccards content
data class Topiccard(
    val title: String,
    val url: String,
    val img: String
)

//containers for data after separating json file
//data class Show and ShowContainer done by Julian
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
//include null ability because errors could return null locations, want to display an usr msg if so
//initialised with null because la(0.0), lo(0.0) is also a location (African ocean/shore area)
data class LocationDetails(
    val latitude: Double?,
    val longitude: Double?
)
