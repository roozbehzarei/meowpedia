package com.roozbehzarei.meowpedia.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity

@Dao
interface BreedDao {

    @Upsert
    suspend fun upsertAll(breeds: List<BreedEntity>)

    @Query("SELECT * FROM breed WHERE id = :id")
    suspend fun getById(id: String): BreedEntity

    @Query("UPDATE breed SET image_id = :imageUrl WHERE reference_image_id = :imageKey")
    suspend fun updateImage(imageKey: String, imageUrl: String)

    @Query("SELECT * FROM breed")
    fun pagingSource(): PagingSource<Int, BreedEntity>

    @Query("DELETE FROM breed")
    suspend fun clearAll()

}