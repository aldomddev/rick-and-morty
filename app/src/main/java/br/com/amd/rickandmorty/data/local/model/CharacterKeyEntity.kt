package br.com.amd.rickandmorty.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_keys")
data class CharacterKeyEntity(
    @PrimaryKey val id: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
