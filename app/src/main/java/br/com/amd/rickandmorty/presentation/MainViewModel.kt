package br.com.amd.rickandmorty.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import br.com.amd.rickandmorty.data.local.model.toCharacter
import br.com.amd.rickandmorty.data.repository.RickAndMortyRepository
import br.com.amd.rickandmorty.domain.model.Character
import br.com.amd.rickandmorty.presentation.model.CharacterStatusFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val rickAndMortyRepository: RickAndMortyRepository
) : ViewModel() {

    val onNameQueryChange: (String) -> Unit
    val onStatusQueryChange: (CharacterStatusFilter) -> Unit
    val charactersSearchPagingData: Flow<PagingData<Character>>

    init {
        val nameQueryFlow = MutableSharedFlow<String>()
        val nameQuery = nameQueryFlow
            .filterNot { it.isBlank() }
            .onStart { emit("") }

        val statusQueryFlow = MutableSharedFlow<CharacterStatusFilter>()
        val statusQuery = statusQueryFlow
            .distinctUntilChanged()
            .onStart { emit(CharacterStatusFilter.ALL) }

        charactersSearchPagingData = combine(
            nameQuery,
            statusQuery,
            ::Pair
        ).flatMapLatest { (name, status) ->
            rickAndMortyRepository.getSearchCharactersResultStream(
                name = name,
                status = status.query
            )
        }.cachedIn(viewModelScope)

        onNameQueryChange = { name ->
            viewModelScope.launch { nameQueryFlow.emit(name) }
        }

        onStatusQueryChange = { status ->
            viewModelScope.launch { statusQueryFlow.emit(status) }
        }
    }

    val charactersListPagingData = rickAndMortyRepository
        .getCharactersListStream()
        .map { pagingData ->
            pagingData.map { characterEntity -> characterEntity.toCharacter() }
        }
        .cachedIn(viewModelScope)
}