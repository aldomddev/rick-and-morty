package br.com.amd.rickandmorty.data.remote.model

import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    val id: Int,
    val name: String,
    val status: String,
    val image: String
)

fun CharacterResponse.toCharacterEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    image = image
)

fun List<CharacterResponse>.toCharactersEntity() = map { it.toCharacterEntity() }
