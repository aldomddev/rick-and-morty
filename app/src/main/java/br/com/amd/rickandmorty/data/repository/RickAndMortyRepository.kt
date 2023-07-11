package br.com.amd.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.amd.rickandmorty.data.local.database.RickAndMortyDatabase
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.data.paging.CharactersRemoteMediator
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
            rickAndMortyApi.getCharacterDetails(id).toCharacter()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    suspend fun getSearchCharactersResultStream(
        name: String,
        status: String
    ): Flow<PagingData<CharacterEntity>> {

        val pagingSourceFactory = {
            rickAndMortyDb.getCharactersDao().charsByNameAndStatus(name, status)
        }

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = CharactersRemoteMediator(
                rickAndMortyDb = rickAndMortyDb,
                rickAndMortyApi = rickAndMortyApi
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}