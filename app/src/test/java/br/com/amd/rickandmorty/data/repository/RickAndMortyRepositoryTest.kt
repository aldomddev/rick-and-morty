package br.com.amd.rickandmorty.data.repository

import br.com.amd.rickandmorty.data.local.database.RickAndMortyDatabase
import br.com.amd.rickandmorty.data.remote.api.RickAndMortyApi
import br.com.amd.rickandmorty.utils.CharacterFactory
import br.com.amd.rickandmorty.utils.toCharacterEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RickAndMortyRepositoryTest {

    private val rickAndMortyApi: RickAndMortyApi = mockk()
    private val rickAndMortyDb: RickAndMortyDatabase = mockk()

    private lateinit var repository: RickAndMortyRepository

    @Before
    fun setup() {
        repository = RickAndMortyRepositoryImpl(
            rickAndMortyApi = rickAndMortyApi,
            rickAndMortyDb = rickAndMortyDb
        )
    }

    @Test
    fun `get character details should succeed`() = runTest {
        coEvery {
            rickAndMortyDb.charactersDao().charById(any())
        } answers { CharacterFactory.create(id = 1, name = "", status = "").toCharacterEntity() }

        val result = repository.getCharacterDetails(id = 1)

        assert(result.isSuccess)
        coVerify { rickAndMortyDb.charactersDao().charById(id = 1) }
    }

    @Test
    fun `get character details should fail if no matching id is found`() = runTest {
        coEvery {
            rickAndMortyDb.charactersDao().charById(any())
        } answers { null }

        val result = repository.getCharacterDetails(id = 1)

        assert(result.isFailure)
        coVerify { rickAndMortyDb.charactersDao().charById(id = 1) }
    }
}
