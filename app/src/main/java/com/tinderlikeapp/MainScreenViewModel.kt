package com.tinderlikeapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinderlikeapp.data.models.character.APICharactersRequest
import com.tinderlikeapp.data.network.repository.CharactersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository
): ViewModel() {

    private val _stateFlow= MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            val response = charactersRepository.getCharacters(
                APICharactersRequest(
                    10,
                    1,
                    null,
                    null
                )
            )
            Log.d("MainScreenViewModel", "getCharacters: $response")
        }
    }

}