package com.tinderlikeapp.data.network.repository

import com.tinderlikeapp.data.models.character.APICharactersRequest
import com.tinderlikeapp.data.models.character.APICharactersResponse
import com.tinderlikeapp.data.network.APIResult
import com.tinderlikeapp.data.network.service.RickyAndMortyApiService
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val charactersApi: RickyAndMortyApiService
) : CharactersRepository {
    override suspend fun getCharacters(
        request: APICharactersRequest
    ): APIResult<APICharactersResponse> {
        return charactersApi.getCharacters(
            page = request.pages,
            counts = request.counts,
            next = request.next,
            prev = request.prev
        )
    }
}