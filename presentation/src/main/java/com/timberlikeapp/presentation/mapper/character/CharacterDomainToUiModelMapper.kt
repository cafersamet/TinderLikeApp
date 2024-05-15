package com.timberlikeapp.presentation.mapper.character

import com.timberlikeapp.presentation.model.CharacterUiModel
import com.tinderlikeapp.domain.model.CharacterDomainModel
import javax.inject.Inject

interface CharacterDomainToUiModelMapper {
    fun mapToUiModel(domainList: List<CharacterDomainModel>): Map<String, CharacterUiModel>
}

class CharacterDomainToUiModelMapperImpl @Inject constructor() : CharacterDomainToUiModelMapper {
    override fun mapToUiModel(
        domainList: List<CharacterDomainModel>
    ): Map<String, CharacterUiModel> {
        return domainList.map { domainModel ->
            CharacterUiModel(
                id = domainModel.id.toString(),
                name = domainModel.name,
                imageUrl = domainModel.image,
                status = domainModel.status,
                location = domainModel.location
            )
        }.associateBy { it.id }
    }
}