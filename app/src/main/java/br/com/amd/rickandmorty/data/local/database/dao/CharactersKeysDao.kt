package br.com.amd.rickandmorty.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.amd.rickandmorty.data.local.model.CharacterKeyEntity

@Dao
interface CharactersKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<CharacterKeyEntity>)

    @Query("SELECT * FROM character_keys WHERE id = :musicId")
    fun getCharacterKey(musicId: Int): CharacterKeyEntity?

    @Query("DELETE FROM character_keys")
    suspend fun clearAll()
}
