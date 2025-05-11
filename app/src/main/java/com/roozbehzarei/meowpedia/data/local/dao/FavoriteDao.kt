package com.roozbehzarei.meowpedia.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.roozbehzarei.meowpedia.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun getAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite WHERE id = :id")
    suspend fun getByIdOrNull(id: String): FavoriteEntity?

    @Upsert()
    suspend fun upsert(favorite: FavoriteEntity)

}