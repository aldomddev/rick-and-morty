package br.com.amd.rickandmorty.data.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.amd.rickandmorty.data.local.model.CharacterEntity

@Dao
interface CharactersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters ORDER BY id ASC")
    fun pagingSource(): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM characters WHERE id LIKE :id")
    suspend fun charById(id: Int): CharacterEntity?

    @Query(
        "SELECT * FROM characters WHERE " +
        "name LIKE :name AND status LIKE :status"
    )
    fun charsByNameAndStatus(
        name: String,
        status: String
    ): PagingSource<Int, CharacterEntity>

    @Query("DELETE FROM characters")
    suspend fun clearAll()
}