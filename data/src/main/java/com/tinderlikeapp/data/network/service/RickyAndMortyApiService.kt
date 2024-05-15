package com.tinderlikeapp.data.network.service

import com.tinderlikeapp.data.models.character.APICharactersResponse
import com.tinderlikeapp.data.network.APIResult
import retrofit2.http.GET
import retrofit2.http.Query

interface RickyAndMortyApiService {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
    ): APIResult<APICharactersResponse>

}