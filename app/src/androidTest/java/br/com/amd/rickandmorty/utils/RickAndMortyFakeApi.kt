package br.com.amd.rickandmorty.utils

import android.content.Context
import br.com.amd.rickandmorty.data.remote.api.RickAndMortyApi
import br.com.amd.rickandmorty.data.remote.model.CharacterPageInfo
import br.com.amd.rickandmorty.data.remote.model.CharacterPageResponse
import br.com.amd.rickandmorty.data.remote.model.CharacterResponse
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.stream.Collectors

class RickAndMortyFakeApi(private val context: Context) : RickAndMortyApi {

    var fakeResponse = CharacterPageResponse(info = CharacterPageInfo(), results = emptyList())
    var throwOnNextCall = false

    override suspend fun getCharacters(
        page: Int,
        name: String?,
        status: String?
    ): CharacterPageResponse {
        if (throwOnNextCall) {
            throwOnNextCall = false
            throw IOException()
        }

        return fakeResponse
    }

    override suspend fun getCharacterDetails(id: Int): CharacterResponse {
        if (throwOnNextCall) {
            throwOnNextCall = false
            throw IOException()
        }

        return fakeResponse.results[id]
    }

    fun init() {
        val inputStream = context.assets.open("characters_page_response.json")
        val json = BufferedReader(InputStreamReader(inputStream))
            .lines()
            .collect(Collectors.joining(""))
        val jsonDecoder = Json { ignoreUnknownKeys = true }
        fakeResponse = jsonDecoder.decodeFromString(json)
    }
}
