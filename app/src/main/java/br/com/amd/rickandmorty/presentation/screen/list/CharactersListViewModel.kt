package br.com.amd.rickandmorty.presentation.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import br.com.amd.rickandmorty.data.local.model.toCharacter
import br.com.amd.rickandmorty.data.repository.RickAndMortyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CharactersListViewModel @Inject constructor(
    private val rickAndMortyRepository: RickAndMortyRepository
) : ViewModel() {

    val charactersListPagingData = rickAndMortyRepository
        .getCharactersListStream()
        .map { pagingData ->
            pagingData.map { characterEntity -> characterEntity.toCharacter() }
        }
        .cachedIn(viewModelScope)
}