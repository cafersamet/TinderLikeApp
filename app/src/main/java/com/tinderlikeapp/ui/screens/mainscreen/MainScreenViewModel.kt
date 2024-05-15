package com.tinderlikeapp.ui.screens.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timberlikeapp.presentation.listsource.character.CharacterListSource
import com.tinderlikeapp.domain.usecase.GetCharacterUseCaseResult
import com.tinderlikeapp.domain.usecase.GetCharactersUseCase
import com.tinderlikeapp.domain.usecase.GetCharactersUseCaseParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PAGINATION_THRESHOLD = 10

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val charactersListSource: CharacterListSource,
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _viewStateFlow = MutableStateFlow(MainViewState.initial())
    val viewStateFlow = _viewStateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<MainScreenEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getCharacters()
    }

    private fun getCharacters() {
        viewModelScope.launch {
            val param = GetCharactersUseCaseParam(
                page = 1
            )
            getCharactersUseCase
                .execute(param)
                .asFlow()
                .onStart {
                    viewModelScope.launch {
                        _viewStateFlow.update {
                            it.copy(
                                charactersUiList = emptyList(),
                                next = null,
                                page = 1,
                                showNoData = false,
                                isLoading = true
                            )
                        }
                    }
                }
                .collect { useCaseResult ->
                    when (useCaseResult) {
                        is GetCharacterUseCaseResult.Success -> {
                            charactersListSource.addList(useCaseResult.characters)
                            _viewStateFlow.update {
                                it.copy(
                                    charactersUiList = charactersListSource.characterList,
                                    next = useCaseResult.next,
                                    page = 2,
                                    isLoading = false,
                                    showNoData = charactersListSource.characterList.isEmpty()
                                )
                            }
                        }

                        is GetCharacterUseCaseResult.Error -> {
                            useCaseResult.message?.let {
                                _eventFlow.emit(MainScreenEvent.ShowToast(it))
                            }
                            _viewStateFlow.update {
                                it.copy(
                                    showNoData = true,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun getCharactersWithNext() {
        val next = viewStateFlow.value.next ?: return
        viewModelScope.launch {
            val param = GetCharactersUseCaseParam(
                page = viewStateFlow.value.page,
                next = next
            )
            getCharactersUseCase
                .execute(param)
                .asFlow()
                .collect { useCaseResult ->
                    when (useCaseResult) {
                        is GetCharacterUseCaseResult.Success -> {
                            charactersListSource.addList(useCaseResult.characters)
                            _viewStateFlow.update {
                                it.copy(
                                    charactersUiList = charactersListSource.characterList,
                                    page = viewStateFlow.value.page + 1,
                                    next = viewStateFlow.value.next
                                )
                            }
                        }

                        is GetCharacterUseCaseResult.Error -> {
                            useCaseResult.message?.let {
                                _eventFlow.emit(MainScreenEvent.ShowToast(it))
                            }
                        }
                    }
                }
        }
    }

    fun onAction(actionIntent: MainScreenIntent) {
        when (actionIntent) {
            is MainScreenIntent.GetCharacters -> getCharacters()
            is MainScreenIntent.OnSwipeLeft -> removeCharacter(actionIntent.id)
            is MainScreenIntent.OnSwipeRight -> removeCharacter(actionIntent.id)
            MainScreenIntent.OnRefresh -> getCharacters()
        }
    }

    private fun removeCharacter(id: String) {
        charactersListSource.removeItem(id)
        viewModelScope.launch {
            if (charactersListSource.characterList.size < PAGINATION_THRESHOLD) {
                getCharactersWithNext()
            }
            _viewStateFlow.update {
                it.copy(
                    charactersUiList = charactersListSource.characterList,
                    showNoData = charactersListSource.characterList.isEmpty()
                )
            }
        }
    }

}