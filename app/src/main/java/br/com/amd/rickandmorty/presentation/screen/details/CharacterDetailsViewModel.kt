package br.com.amd.rickandmorty.presentation.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amd.rickandmorty.data.repository.RickAndMortyRepository
import br.com.amd.rickandmorty.di.IoDispatcher
import br.com.amd.rickandmorty.domain.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val rickAndMortyRepository: RickAndMortyRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _viewState =
        MutableStateFlow<CharacterDetailsUiState>(CharacterDetailsUiState.Loading)
    val viewState: StateFlow<CharacterDetailsUiState> = _viewState.asStateFlow()

    fun getCharacterDetails(id: Int) {
        _viewState.value = CharacterDetailsUiState.Loading

        viewModelScope.launch(dispatcher) {
            rickAndMortyRepository
                .getCharacterDetails(id)
                .fold(
                    onSuccess = ::onGetCharacterDataSuccess,
                    onFailure = { onGetCharacterDataFailure() }
                )
        }
    }

    private fun onGetCharacterDataSuccess(character: Character) {
        _viewState.value = CharacterDetailsUiState.Success(character)
    }

    private fun onGetCharacterDataFailure() {
        _viewState.value = CharacterDetailsUiState.Error
    }
}
