package com.timberlikeapp.presentation.listsource.character

import android.util.Log
import com.timberlikeapp.presentation.mapper.character.CharacterDomainToUiModelMapper
import com.timberlikeapp.presentation.model.CharacterUiModel
import com.tinderlikeapp.domain.model.CharacterDomainModel
import com.tinderlikeapp.utils.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterListSource @Inject constructor(
    private val characterDomainToUiModelMapper: CharacterDomainToUiModelMapper,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    private val _characterMap = linkedMapOf<String, CharacterUiModel>()
    val characterList: List<CharacterUiModel> get() = _characterMap.values.toList().asReversed()

    suspend fun addList(domainList: List<CharacterDomainModel>) {

        val uiModelMap = withContext(defaultDispatcher) {
            characterDomainToUiModelMapper.mapToUiModel(domainList)
        }
        _characterMap.putAll(uiModelMap)
        Log.d("CharacterListSource", "addList: ${_characterMap.values.map { it.id }}")
    }

    fun removeItem(id: String) {
        _characterMap.remove(id)
        Log.d("CharacterListSource", "removeItem: ${_characterMap.values.map { it.id }}")
    }

}