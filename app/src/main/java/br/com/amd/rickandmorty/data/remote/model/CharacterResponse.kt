package br.com.amd.rickandmorty.data.remote.model

import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.domain.model.Character
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String
) {
    @Serializable
    data class Origin(
        val name: String
    )

    @Serializable
    data class Location(
        val name: String
    )
}

fun CharacterResponse.toCharacterEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    origin = origin.name,
    location = location.name,
    image = image
)

fun List<CharacterResponse>.toCharactersEntity() = map { it.toCharacterEntity() }

fun CharacterResponse.toCharacter() = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    origin = origin.name,
    location = location.name,
    image = image
)

fun List<CharacterResponse>.toCharacters() = map { it.toCharacter() }