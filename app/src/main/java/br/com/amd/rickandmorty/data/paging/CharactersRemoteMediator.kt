package br.com.amd.rickandmorty.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.com.amd.rickandmorty.data.local.database.RickAndMortyDatabase
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.data.local.model.CharacterKeyEntity
import br.com.amd.rickandmorty.data.remote.api.RickAndMortyApi
import br.com.amd.rickandmorty.data.remote.model.toCharactersEntity
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class CharactersRemoteMediator(
    private val nameQuery: String? = null,
    private val statusQuery: String? = null,
    private val rickAndMortyApi: RickAndMortyApi,
    private val rickAndMortyDb: RickAndMortyDatabase
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {

        val page = when (loadType) {

            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(RickAndMortyApi.PAGE_SIZE)
                    ?: RickAndMortyApi.DEFAULT_FIRST_PAGE
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        return try {
            val remotePageData = rickAndMortyApi.getCharacters(
                page = page,
                name = nameQuery,
                status = statusQuery
            )
            val endOfPagination = remotePageData.info.next == null

            rickAndMortyDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    rickAndMortyDb.charactersKeysDao().clearAll()
                    rickAndMortyDb.charactersDao().clearAll()
                }

                val prevKey = if (page == RickAndMortyApi.DEFAULT_FIRST_PAGE) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1


                val characters = remotePageData.results.toCharactersEntity()
                val keys = characters.map { character ->
                    CharacterKeyEntity(id = character.id, nextKey = nextKey, prevKey = prevKey)
                }

                println("AMD - inserting ${characters.size} chars")

                rickAndMortyDb.charactersKeysDao().insertAll(keys)
                rickAndMortyDb.charactersDao().insertAll(characters)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, CharacterEntity>
    ): CharacterKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { charId ->
                rickAndMortyDb.charactersKeysDao().getCharacterKey(charId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, CharacterEntity>
    ): CharacterKeyEntity? {
        return state.pages.firstOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { characterEntity ->
            rickAndMortyDb.charactersKeysDao().getCharacterKey(characterEntity.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, CharacterEntity>
    ): CharacterKeyEntity? {
        return state.pages.lastOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { characterEntity ->
            rickAndMortyDb.charactersKeysDao().getCharacterKey(characterEntity.id)
        }
    }
}