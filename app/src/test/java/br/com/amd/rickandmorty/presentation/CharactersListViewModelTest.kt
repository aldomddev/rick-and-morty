package br.com.amd.rickandmorty.presentation

import androidx.paging.AsyncPagingDataDiffer
import app.cash.turbine.test
import br.com.amd.rickandmorty.data.repository.RickAndMortyFakeRepository
import br.com.amd.rickandmorty.domain.model.Character
import br.com.amd.rickandmorty.presentation.model.CharacterStatusFilter
import br.com.amd.rickandmorty.presentation.screen.list.CharactersListViewModel
import br.com.amd.rickandmorty.utils.CharacterFactory
import br.com.amd.rickandmorty.utils.ListUpdateTestCallback
import br.com.amd.rickandmorty.utils.MainDispatcherRule
import br.com.amd.rickandmorty.utils.NoDiffCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharactersListViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val helperCache = listOf(
        CharacterFactory.create(id = 1, name = "Rick", status = "Alive"),
        CharacterFactory.create(id = 2, name = "Morty", status = "Alive")
    )

    private val rickAndMortyRepository = RickAndMortyFakeRepository()
    private lateinit var differHelper: AsyncPagingDataDiffer<Character>
    private lateinit var viewModel: CharactersListViewModel

    @Before
    fun setup() {
        initFakeRepository()

        viewModel = CharactersListViewModel(
            rickAndMortyRepository = rickAndMortyRepository,
            ioDispatcher = dispatcherRule.testDispatcher
        )

        differHelper = AsyncPagingDataDiffer(
            diffCallback = NoDiffCallback(),
            updateCallback = ListUpdateTestCallback(),
            workerDispatcher = Dispatchers.Main
        )
    }

    @Test
    fun `list of characters should be emitted if there is data`() = runTest {
        viewModel.charactersSearchPagingData.test {
            val expectedData = helperCache

            val actualData = awaitItem()
            differHelper.submitData(actualData) // help unwrap a Paging<Character>

            assertEquals(expectedData, differHelper.snapshot().items)
        }
    }

    @Test
    fun `list should be updated when search by name returns data`() = runTest {
        viewModel.charactersSearchPagingData.test {
            val expectedData = listOf(CharacterFactory.create(id = 1, name = "Rick"))

            viewModel.onNameQueryChange("Rick")

            val actualData = expectMostRecentItem()
            differHelper.submitData(actualData)

            assertEquals(expectedData, differHelper.snapshot().items)
        }
    }

    @Test
    fun `list should be updated when search by status returns data`() = runTest {
        viewModel.charactersSearchPagingData.test {
            val expectedData = helperCache

            viewModel.onStatusQueryChange(CharacterStatusFilter.ALIVE)

            val actualData = expectMostRecentItem()
            differHelper.submitData(actualData)

            assertEquals(expectedData, differHelper.snapshot().items)
        }
    }

    private fun initFakeRepository() {
        if (rickAndMortyRepository.characters.isEmpty()) {
            rickAndMortyRepository.characters.addAll(helperCache)
        }
    }
}
