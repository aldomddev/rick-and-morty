package br.com.amd.rickandmorty.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.amd.rickandmorty.data.local.CharactersDao
import br.com.amd.rickandmorty.data.local.model.CharacterEntity

@Database(
    entities = [CharacterEntity::class],
    exportSchema = true,
    version = 1
)
abstract class RickAndMortyDatabase: RoomDatabase() {
    abstract fun getCharactersDao(): CharactersDao

    companion object {
        const val DATABASE_NAME = "rick_and_morty.db"
    }
}