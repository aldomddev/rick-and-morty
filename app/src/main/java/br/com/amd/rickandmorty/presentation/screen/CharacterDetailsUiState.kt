package br.com.amd.rickandmorty.presentation.screen

import br.com.amd.rickandmorty.domain.model.Character

sealed class CharacterDetailsUiState {
    object Loading : CharacterDetailsUiState()
    data class Success(val data: Character) : CharacterDetailsUiState()
    object Error : CharacterDetailsUiState()
}
