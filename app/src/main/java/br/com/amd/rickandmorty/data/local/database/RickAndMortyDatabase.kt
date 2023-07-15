package br.com.amd.rickandmorty.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.amd.rickandmorty.data.local.database.dao.CharactersKeysDao
import br.com.amd.rickandmorty.data.local.database.dao.CharactersDao
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.data.local.model.CharacterKeyEntity

@Database(
    entities = [CharacterEntity::class, CharacterKeyEntity::class],
    exportSchema = true,
    version = 1
)
abstract class RickAndMortyDatabase: RoomDatabase() {
    abstract fun charactersKeysDao(): CharactersKeysDao
    abstract fun charactersDao(): CharactersDao

    companion object {
        const val DATABASE_NAME = "rick_and_morty.db"
    }
}