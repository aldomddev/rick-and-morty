package br.com.amd.rickandmorty.presentation

import br.com.amd.rickandmorty.data.repository.RickAndMortyFakeRepository
import br.com.amd.rickandmorty.presentation.screen.details.CharacterDetailsUiState
import br.com.amd.rickandmorty.presentation.screen.details.CharacterDetailsViewModel
import br.com.amd.rickandmorty.utils.CharacterFactory
import br.com.amd.rickandmorty.utils.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharacterDetailsViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val rickAndMortyRepository = RickAndMortyFakeRepository()
    private lateinit var viewModel: CharacterDetailsViewModel

    @Before
    fun setup() {
        rickAndMortyRepository.characters.clear()

        viewModel = CharacterDetailsViewModel(
            rickAndMortyRepository = rickAndMortyRepository,
            dispatcher = dispatcherRule.testDispatcher
        )
    }

    @Test
    fun `state Is Initially Loading`() = runTest {
        assertEquals(CharacterDetailsUiState.Loading, viewModel.viewState.value)
    }

    @Test
    fun `search by a valid id - verify state is success`() = runTest {
        rickAndMortyRepository.characters.add(CharacterFactory.create(id = 1, name = "Rick"))

        viewModel.getCharacterDetails(id = 1)
        val result = viewModel.viewState.value

        assertTrue(result is CharacterDetailsUiState.Success)
    }

    @Test
    fun `search by an invalid id - verify state is error`() = runTest {
        viewModel.getCharacterDetails(id = 1)
        val result = viewModel.viewState.value

        assertTrue(result is CharacterDetailsUiState.Error)
    }
}
