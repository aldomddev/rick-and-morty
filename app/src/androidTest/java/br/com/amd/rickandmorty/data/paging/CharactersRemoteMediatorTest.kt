package br.com.amd.rickandmorty.data.paging

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.amd.rickandmorty.data.local.database.RickAndMortyDatabase
import br.com.amd.rickandmorty.data.local.database.dao.CharactersDao
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.data.paging.repository.RickAndMortyFakeApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
@RunWith(AndroidJUnit4::class)
class CharactersRemoteMediatorTest {

    private lateinit var userDao: CharactersDao
    private lateinit var db: RickAndMortyDatabase
    private lateinit var fakeApi: RickAndMortyFakeApi

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RickAndMortyDatabase::class.java
        ).build()

        fakeApi = RickAndMortyFakeApi()
        userDao = db.charactersDao()
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        fakeApi.init()
        val remoteMediator = CharactersRemoteMediator(
            nameQuery = null,
            statusQuery = null,
            rickAndMortyApi = fakeApi,
            rickAndMortyDb = db,
        )

        val pagingState = PagingState<Int, CharacterEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(20),
            leadingPlaceholderCount = 10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runTest {
        val remoteMediator = CharactersRemoteMediator(
            nameQuery = null,
            statusQuery = null,
            rickAndMortyApi = fakeApi,
            rickAndMortyDb = db,
        )

        val pagingState = PagingState<Int, CharacterEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(20),
            leadingPlaceholderCount = 10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runTest {
        fakeApi.apply {
            init()
            throwOnNextCall = true
        }
        val remoteMediator = CharactersRemoteMediator(
            nameQuery = null,
            statusQuery = null,
            rickAndMortyApi = fakeApi,
            rickAndMortyDb = db,
        )

        val pagingState = PagingState<Int, CharacterEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(20),
            leadingPlaceholderCount = 10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.clearAllTables()
        db.close()
    }
}