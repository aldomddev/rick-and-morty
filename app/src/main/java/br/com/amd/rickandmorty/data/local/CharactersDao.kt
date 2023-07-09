package br.com.amd.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Query
import br.com.amd.rickandmorty.data.local.model.CharacterEntity

@Dao
interface CharactersDao {
    @Query("SELECT * FROM characters")
    suspend fun getAll(): List<CharacterEntity>
}