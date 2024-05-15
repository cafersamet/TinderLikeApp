package com.timberlikeapp.presentation.model

data class CharacterUiModel(
    override val id: String,
    val name: String,
    val imageUrl: String,
    val status: String,
    val location: String
) : UiModel