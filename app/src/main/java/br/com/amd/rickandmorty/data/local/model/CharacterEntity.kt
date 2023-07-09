package br.com.amd.rickandmorty.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.amd.rickandmorty.data.remote.model.CharacterResponse

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "image_url")
    val image: String
)

fun CharacterResponse.toCharacterEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    image = image
)

fun List<CharacterResponse>.toCharactersEntity() = map { it.toCharacterEntity() }