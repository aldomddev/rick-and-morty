package br.com.amd.rickandmorty.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.data.local.model.toCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    charactersPaging: Pager<Int, CharacterEntity>
) : ViewModel() {

    val charactersPagingData = charactersPaging
        .flow
        .map { pagingData ->
            pagingData.map { characterEntity -> characterEntity.toCharacter() }
        }
        .cachedIn(viewModelScope)
}