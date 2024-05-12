package com.tinderlikeapp.domain.usecase

import com.tinderlikeapp.data.di.DefaultDispatcher
import com.tinderlikeapp.data.models.character.APICharactersRequest
import com.tinderlikeapp.data.network.APIResult
import com.tinderlikeapp.data.network.repository.CharactersRepository
import com.tinderlikeapp.domain.UseCase
import com.tinderlikeapp.domain.UseCaseParam
import com.tinderlikeapp.domain.UseCaseResult
import com.tinderlikeapp.domain.mapper.CharacterEntityToDomainModelMapper
import com.tinderlikeapp.domain.model.CharacterDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharactersRepository,
    private val characterEntityToDomainModelMapper: CharacterEntityToDomainModelMapper,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : UseCase<GetCharactersUseCaseParam, GetCharacterUseCaseResult> {
    override fun execute(
        param: GetCharactersUseCaseParam
    ): suspend () -> GetCharacterUseCaseResult {
        return suspend {
            val request = APICharactersRequest(
                counts = param.counts,
                pages = param.pages,
                next = param.next,
                prev = param.prev
            )
            when (val result = repository.getCharacters(request)) {
                is APIResult.Success -> {
                    val response = result.data
                    val domainList = withContext(dispatcher) {
                        characterEntityToDomainModelMapper.mapToDomainModel(response.results)
                    }
                    GetCharacterUseCaseResult.Success(
                        domainList,
                        response.info.next,
                        response.info.prev
                    )
                }

                is APIResult.Error -> {
                    val error = result.apiError.message
                    GetCharacterUseCaseResult.Error(error)
                }
            }
        }
    }
}

data class GetCharactersUseCaseParam(
    val counts: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null
) : UseCaseParam

sealed interface GetCharacterUseCaseResult : UseCaseResult {
    data class Success(
        val characters: List<CharacterDomainModel>,
        val next: String?,
        val prev: String?
    ) : GetCharacterUseCaseResult

    data class Error(val message: String?) : GetCharacterUseCaseResult
}