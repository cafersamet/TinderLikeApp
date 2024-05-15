package com.tinderlikeapp.ui.screens.mainscreen

import androidx.compose.runtime.Stable
import com.timberlikeapp.presentation.model.CharacterUiModel

@Stable
data class MainViewState(
    val charactersUiList: List<CharacterUiModel>,
    val next: String?,
    val page: Int,
    val showNoData: Boolean,
    val noDataMessage: String,
    val isLoading: Boolean,
    val loadingMessage: String
) {
    companion object {
        fun initial() = MainViewState(
            charactersUiList = emptyList(),
            next = null,
            page = 1,
            showNoData = false,
            noDataMessage = "No More Data",
            isLoading = false,
            loadingMessage = "Loading..."
        )
    }
}