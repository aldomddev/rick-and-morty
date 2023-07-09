package br.com.amd.rickandmorty.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterPageResponse(
    val results: List<CharacterResponse>
)
