package com.roozbehzarei.meowpedia.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.roozbehzarei.meowpedia.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing favorite cat entities in the local database.
 *
 * Provide methods for interacting with the `favorite` table, allowing for
 * retrieving, inserting, and updating favorite breeds.
 */
@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun getAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite WHERE id = :id")
    suspend fun getByIdOrNull(id: String): FavoriteEntity?

    @Upsert()
    suspend fun upsert(favorite: FavoriteEntity)

}