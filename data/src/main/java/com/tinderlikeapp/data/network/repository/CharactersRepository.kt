package com.tinderlikeapp.data.network.repository

import com.tinderlikeapp.data.models.character.APICharactersRequest
import com.tinderlikeapp.data.models.character.APICharactersResponse
import com.tinderlikeapp.data.network.APIResult

interface CharactersRepository {
    suspend fun getCharacters(
        request: APICharactersRequest
    ): APIResult<APICharactersResponse>
}