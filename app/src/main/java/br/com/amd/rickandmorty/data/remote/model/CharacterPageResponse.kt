package br.com.amd.rickandmorty.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterPageResponse(
    val info: CharacterPageInfo,
    val results: List<CharacterResponse>
)

@Serializable
data class CharacterPageInfo(
    val next: String? = null
)
