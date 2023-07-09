package br.com.amd.rickandmorty.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    val id: Int,
    val name: String,
    val status: String,
    val image: String
)
