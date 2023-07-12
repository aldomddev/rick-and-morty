package br.com.amd.rickandmorty.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.data.local.model.toCharacter
import br.com.amd.rickandmorty.data.repository.RickAndMortyRepository
import br.com.amd.rickandmorty.domain.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val rickAndMortyRepository: RickAndMortyRepository,
    charactersPaging: Pager<Int, CharacterEntity>,
) : ViewModel() {

    val search: (String) -> Unit
    val pagingDataFlow: Flow<PagingData<Character>>

    init {
        val searchStateFlow = MutableSharedFlow<String>()
        val searches = searchStateFlow
            .filterNotNull()
            .distinctUntilChanged()
            .onStart { emit("") }

        pagingDataFlow = searches
            .filterNot { it.isBlank() }
            .flatMapLatest { searchString ->
                rickAndMortyRepository.getSearchCharactersResultStream(
                    name = searchString,
                    status = "Alive"
                )
            }
            .cachedIn(viewModelScope)

        search = {
            viewModelScope.launch { searchStateFlow.emit(it) }
        }
    }

    val charactersPagingData = charactersPaging
        .flow
        .map { pagingData ->
            pagingData.map { characterEntity -> characterEntity.toCharacter() }
        }
        .cachedIn(viewModelScope)
}