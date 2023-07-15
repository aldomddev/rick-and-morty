package br.com.amd.rickandmorty.data.repository

import androidx.paging.PagingData
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface RickAndMortyRepository {
    suspend fun getCharacterDetails(id: Int): Result<Character>
    fun getCharactersListStream(): Flow<PagingData<CharacterEntity>>
    suspend fun getSearchCharactersResultStream(
        name: String,
        status: String
    ): Flow<PagingData<CharacterEntity>>
}