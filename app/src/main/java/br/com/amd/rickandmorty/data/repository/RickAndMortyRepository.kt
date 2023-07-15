package br.com.amd.rickandmorty.data.repository

import androidx.paging.PagingData
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface RickAndMortyRepository {
    suspend fun getCharacterDetails(id: Int): Result<Character>
    suspend fun getCharactersListStream(): Flow<PagingData<CharacterEntity>>
    suspend fun getSearchCharactersResultStream(
        name: String? = null,
        status: String? = null
    ): Flow<PagingData<CharacterEntity>>
}