package br.com.amd.rickandmorty.data.remote.api

import br.com.amd.rickandmorty.data.remote.model.CharacterPageResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface RickAndMortyApi {
    @GET("character/")
    suspend fun getCharacters(@Query("page") page: Int = DEFAULT_FIRST_PAGE): CharacterPageResponse

    companion object {
        const val BASE_API = "https://rickandmortyapi.com/api/"
        const val DEFAULT_FIRST_PAGE = 1
    }
}