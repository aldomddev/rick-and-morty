package br.com.amd.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.amd.rickandmorty.data.local.database.RickAndMortyDatabase
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.data.local.model.toCharacter
import br.com.amd.rickandmorty.data.paging.CharactersRemoteMediator
import br.com.amd.rickandmorty.data.paging.CharactersSearchPagingSource
import br.com.amd.rickandmorty.data.remote.api.RickAndMortyApi
import br.com.amd.rickandmorty.data.remote.api.RickAndMortyApi.Companion.PAGE_SIZE
import br.com.amd.rickandmorty.data.remote.model.toCharacter
import br.com.amd.rickandmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow

class RickAndMortyRepository(
    private val rickAndMortyApi: RickAndMortyApi,
    private val rickAndMortyDb: RickAndMortyDatabase
) {
    suspend fun getCharacterDetails(id: Int): Result<Character> {
        return runCatching {
            val localChar = rickAndMortyDb.getCharactersDao().charById(id)?.toCharacter()
            if (localChar == null) {
                val remoteChar = rickAndMortyApi.getCharacterDetails(id).toCharacter()
                return Result.success(remoteChar)
            }

            return Result.success(localChar)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getCharactersListStream(): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = CharactersRemoteMediator(
                rickAndMortyDb = rickAndMortyDb,
                rickAndMortyApi = rickAndMortyApi
            ),
            pagingSourceFactory = {
                rickAndMortyDb.getCharactersDao().pagingSource()
            }
        ).flow
    }

    fun getSearchCharactersResultStream(
        name: String? = null,
        status: String? = null
    ): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                CharactersSearchPagingSource(
                    rickAndMortyApi,
                    name,
                    status
                )
            }
        ).flow
    }
}