package com.tinderlikeapp.domain.mapper

import com.tinderlikeapp.data.models.character.APICharacter
import com.tinderlikeapp.domain.model.CharacterDomainModel
import javax.inject.Inject

interface CharacterEntityToDomainModelMapper {
    fun mapToDomainModel(entityList: List<APICharacter>): List<CharacterDomainModel>
}

class CharacterEntityToDomainModelMapperImpl @Inject constructor()
    : CharacterEntityToDomainModelMapper {
    override fun mapToDomainModel(
        entityList: List<APICharacter>
    ): List<CharacterDomainModel> {
        return entityList
            .map {
                CharacterDomainModel(
                    id = it.id,
                    name = it.name,
                    image = it.image,
                    location = it.location.name,
                    status = it.status
                )
            }
    }


}