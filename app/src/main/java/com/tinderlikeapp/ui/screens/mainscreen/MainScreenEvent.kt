package com.tinderlikeapp.ui.screens.mainscreen

sealed interface MainScreenEvent {
    data class ShowToast(val message: String): MainScreenEvent
}