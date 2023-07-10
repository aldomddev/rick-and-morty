package br.com.amd.rickandmorty.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.com.amd.rickandmorty.data.local.database.RickAndMortyDatabase
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.data.remote.api.RickAndMortyApi
import br.com.amd.rickandmorty.data.remote.model.toCharactersEntity

@ExperimentalPagingApi
class CharactersRemoteMediator(
    private val rickAndMortyApi: RickAndMortyApi,
    private val rickAndMortyDb: RickAndMortyDatabase
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> RickAndMortyApi.DEFAULT_FIRST_PAGE
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    when (val lastItem = state.lastItemOrNull()) {
                        null -> RickAndMortyApi.DEFAULT_FIRST_PAGE
                        else -> (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val remotePageData = rickAndMortyApi.getCharacters(page = loadKey)
            val characters = remotePageData.results.toCharactersEntity()

            rickAndMortyDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    rickAndMortyDb.getCharactersDao().clearAll()
                }

                rickAndMortyDb.getCharactersDao().insertAll(characters)
            }

            MediatorResult.Success(
                endOfPaginationReached = remotePageData.info.next == null
            )
        } catch (error: Exception) {
            MediatorResult.Error(error)
        }
    }
}