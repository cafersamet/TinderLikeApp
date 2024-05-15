package com.tinderlikeapp.data.models.character

import com.tinderlikeapp.data.network.Entity

data class APICharactersResponse(
    val info: APIInfo,
    val results: List<APICharacter>
): Entity

data class APIInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
): Entity

data class APICharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: APIOrigin,
    val location: APILocation,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
): Entity

data class APIOrigin(
    val name: String,
    val url: String
): Entity

data class APILocation(
    val name: String,
    val url: String
): Entity