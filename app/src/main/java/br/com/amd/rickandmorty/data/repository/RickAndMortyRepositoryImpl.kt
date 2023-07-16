package br.com.amd.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.amd.rickandmorty.data.local.database.RickAndMortyDatabase
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.data.local.model.toCharacter
import br.com.amd.rickandmorty.data.paging.CharactersRemoteMediator
import br.com.amd.rickandmorty.data.remote.api.RickAndMortyApi
import br.com.amd.rickandmorty.data.remote.api.RickAndMortyApi.Companion.PAGE_SIZE
import br.com.amd.rickandmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RickAndMortyRepositoryImpl @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val rickAndMortyDb: RickAndMortyDatabase
) : RickAndMortyRepository {

    override suspend fun getCharacterDetails(id: Int): Result<Character> {
        return runCatching {
            rickAndMortyDb.charactersDao().charById(id)?.toCharacter()!!
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharactersListStream(): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = CharactersRemoteMediator(
                rickAndMortyDb = rickAndMortyDb,
                rickAndMortyApi = rickAndMortyApi
            ),
            pagingSourceFactory = {
                rickAndMortyDb.charactersDao().pagingSource()
            }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getSearchCharactersResultStream(
        name: String,
        status: String
    ): Flow<PagingData<CharacterEntity>> {
        val nameQuery = "%${name.replace(' ', '%')}%"
        val statusQuery = "%${status.replace(' ', '%')}%"

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = CharactersRemoteMediator(
                nameQuery = name,
                statusQuery = status,
                rickAndMortyDb = rickAndMortyDb,
                rickAndMortyApi = rickAndMortyApi
            ),
            pagingSourceFactory = {
                rickAndMortyDb.charactersDao().charsByNameAndStatus(nameQuery, statusQuery)
            }
        ).flow
    }
}
