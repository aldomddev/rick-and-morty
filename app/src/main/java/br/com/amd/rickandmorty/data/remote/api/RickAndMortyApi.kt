package br.com.amd.rickandmorty.data.remote.api

import br.com.amd.rickandmorty.data.remote.model.CharacterPageResponse
import br.com.amd.rickandmorty.data.remote.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RickAndMortyApi {
    @GET("character/")
    suspend fun getCharacters(
        @Query("page") page: Int = DEFAULT_FIRST_PAGE,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null
    ): CharacterPageResponse

    @GET("character/{id}")
    suspend fun getCharacterDetails(
        @Path("id") id: Int
    ): CharacterResponse

    companion object {
        const val BASE_API = "https://rickandmortyapi.com/api/"
        const val DEFAULT_FIRST_PAGE = 1
        // for page size, see https://rickandmortyapi.com/documentation/#info-and-pagination
        const val PAGE_SIZE = 20
    }
}