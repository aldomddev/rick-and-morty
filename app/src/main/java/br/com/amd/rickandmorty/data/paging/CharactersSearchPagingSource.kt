package br.com.amd.rickandmorty.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.amd.rickandmorty.data.remote.api.RickAndMortyApi
import br.com.amd.rickandmorty.data.remote.model.toCharacters
import br.com.amd.rickandmorty.domain.model.Character
import retrofit2.HttpException
import java.io.IOException

class CharactersSearchPagingSource(
    private val rickAndMortyApi: RickAndMortyApi,
    val name: String? = null,
    val status: String? = null
) : PagingSource<Int, Character>() {

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val nextPageNumber = params.key ?: RickAndMortyApi.DEFAULT_FIRST_PAGE
            val response = rickAndMortyApi.getCharacters(
                page = nextPageNumber,
                name = name,
                status = null
            )

            val nextKey = if (response.info.next != null) {
                nextPageNumber + (params.loadSize / RickAndMortyApi.PAGE_SIZE)
            } else {
                null
            }

            LoadResult.Page(
                data = response.results.toCharacters(),
                prevKey = null,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}