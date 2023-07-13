package br.com.amd.rickandmorty.data.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import br.com.amd.rickandmorty.data.remote.model.toCharacters
import br.com.amd.rickandmorty.data.repository.RickAndMortyFakeApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test

class CharactersSearchPagingSourceTest {

    private val fakeApi = RickAndMortyFakeApi().apply { init() }

    @Test
    fun `refresh load returns characters page on successful fetch`() = runTest {
        val pagingSource = CharactersSearchPagingSource(fakeApi)
        val pager = TestPager(
            config = PagingConfig(pageSize = 20),
            pagingSource = pagingSource
        )

        val result = pager.refresh() as PagingSource.LoadResult.Page
        val expectedResult = fakeApi.fakeResponse.results.toCharacters()

        assertThat(result.data, equalTo(expectedResult))
    }

    @Test
    fun `consecutive loads should succeed`() = runTest {
        val pagingSource = CharactersSearchPagingSource(fakeApi)
        val pager = TestPager(
            config = PagingConfig(pageSize = 20),
            pagingSource = pagingSource
        )

        val page = with(pager) {
            refresh()
            append()
            append()
        } as PagingSource.LoadResult.Page

        val expectedResult = fakeApi.fakeResponse.results.toCharacters()

        assertThat(page.data, equalTo(expectedResult))
    }

    @Test
    fun `refresh load returns error when error occurs`() = runTest {
        val pagingSource = CharactersSearchPagingSource(fakeApi)
        val pager = TestPager(
            config = PagingConfig(pageSize = 20),
            pagingSource = pagingSource
        )

        fakeApi.throwOnNextCall = true

        val result = pager.refresh()
        assertTrue(result is PagingSource.LoadResult.Error)

        val page = pager.getLastLoadedPage()
        assertTrue(page == null)
    }
}