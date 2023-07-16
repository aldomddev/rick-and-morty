package br.com.amd.rickandmorty.utils

import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.domain.model.Character

object CharacterFactory {

    fun create(
        id: Int,
        name: String,
        status: String = "Alive"
    ) = Character(
        id = id,
        name = name,
        status = "status",
        species = "species",
        gender = "gender",
        origin = "origin",
        location = "location",
        image = "image"
    )
}

fun Character.toCharacterEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    origin = origin,
    location = location,
    image = image
)

fun List<Character>.toCharactersEntity() = map { it.toCharacterEntity() }
