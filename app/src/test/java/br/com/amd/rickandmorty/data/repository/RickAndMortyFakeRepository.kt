package br.com.amd.rickandmorty.data.repository

import androidx.paging.PagingData
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.domain.model.Character
import br.com.amd.rickandmorty.utils.toCharactersEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RickAndMortyFakeRepository : RickAndMortyRepository {
    var characters = mutableListOf<Character>()

    override suspend fun getCharacterDetails(id: Int): Result<Character> {
        return runCatching {
            characters.find { it.id == id }!!
        }
    }

    override suspend fun getCharactersListStream(): Flow<PagingData<CharacterEntity>> = flow {
        emit(PagingData.from(characters.toCharactersEntity()))
    }

    override suspend fun getSearchCharactersResultStream(
        name: String?,
        status: String?
    ): Flow<PagingData<CharacterEntity>> = flow {
        val filteredItems = mutableListOf<Character>()

        if (!name.isNullOrEmpty() && !status.isNullOrEmpty()) {
            filteredItems.addAll(characters.filter { it.name == name && it.status == status })
        } else if (!name.isNullOrEmpty()) {
            filteredItems.addAll(characters.filter { it.name == name })
        } else if (!status.isNullOrEmpty()) {
            filteredItems.addAll(characters.filter { it.status == status })
        }

        if (filteredItems.isEmpty()) {
            filteredItems.addAll(characters)
        }

        emit(PagingData.from(filteredItems.toCharactersEntity()))
    }
}

