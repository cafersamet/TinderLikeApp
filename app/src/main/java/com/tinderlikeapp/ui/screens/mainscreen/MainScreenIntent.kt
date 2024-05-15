package com.tinderlikeapp.ui.screens.mainscreen

sealed interface MainScreenIntent {
    data object GetCharacters : MainScreenIntent
    data class OnSwipeLeft(val id: String) : MainScreenIntent
    data class OnSwipeRight(val id: String) : MainScreenIntent
    data object OnRefresh : MainScreenIntent
}