package com.tinderlikeapp.data.models.character

data class APICharactersRequest(
    val counts: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)