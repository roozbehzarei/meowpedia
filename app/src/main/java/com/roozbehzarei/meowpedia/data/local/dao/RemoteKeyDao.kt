package com.roozbehzarei.meowpedia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.roozbehzarei.meowpedia.data.local.entity.RemoteKeyEntity

/**
 * Data Access Object (DAO) for managing [RemoteKeyEntity] instances in the database.
 *
 * Provide methods for inserting, retrieving, and deleting RemoteKeyEntity objects,
 * which are used to track pagination information for The Cat API.
 */
@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(remoteKey: RemoteKeyEntity)

    @Query("SELECT * FROM remote_key WHERE id = :id")
    suspend fun getById(id: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_key WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM remote_key")
    suspend fun clearAll()

}