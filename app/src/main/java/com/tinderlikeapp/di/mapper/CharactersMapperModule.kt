package com.tinderlikeapp.di.mapper

import com.timberlikeapp.presentation.mapper.character.CharacterDomainToUiModelMapper
import com.timberlikeapp.presentation.mapper.character.CharacterDomainToUiModelMapperImpl
import com.tinderlikeapp.domain.mapper.CharacterEntityToDomainModelMapper
import com.tinderlikeapp.domain.mapper.CharacterEntityToDomainModelMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class CharactersMapperModule {

    @Binds
    abstract fun bindCharactersDomainToUiModelMapper(
        charactersDomainToUiModelMapper: CharacterDomainToUiModelMapperImpl
    ): CharacterDomainToUiModelMapper

    @Binds
    abstract fun bindCharactersEntityToDomainModelMapper(
        characterEntityToDomainModelMapper: CharacterEntityToDomainModelMapperImpl
    ): CharacterEntityToDomainModelMapper


}