package com.roozbehzarei.meowpedia.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the Breed entity.
 * Provide methods to interact with the local breed data stored in the database.
 */
@Dao
interface BreedDao {

    @Upsert
    suspend fun upsertAll(breeds: List<BreedEntity>)

    @Upsert
    suspend fun insert(breed: BreedEntity)

    @Query(
        """
        UPDATE breed
        SET
        name = :name,
        temperament = :temperament,
        origin = :origin,
        description = :description,
        life_span = :lifeSpan,
        wikipedia_url = :wikipediaUrl,
        reference_image_id = :imageId
        WHERE id = :id
    """
    )
    suspend fun update(
        id: String,
        name: String,
        temperament: String,
        origin: String,
        description: String,
        lifeSpan: String,
        wikipediaUrl: String?,
        imageId: String?
    )

    @Query("SELECT * FROM breed WHERE id = :id")
    fun getById(id: String): Flow<BreedEntity>

    @Query("SELECT * FROM breed WHERE id = :id")
    suspend fun getByIdOrNull(id: String): BreedEntity?

    @Query("SELECT * FROM breed WHERE name LIKE '%' || :query || '%' COLLATE NOCASE")
    suspend fun searchBreeds(query: String): List<BreedEntity>

    @Query("UPDATE breed SET image_id = :imageUrl WHERE reference_image_id = :imageKey")
    suspend fun updateImage(imageKey: String, imageUrl: String)

    @Query("SELECT * FROM breed")
    fun pagingSource(): PagingSource<Int, BreedEntity>

    @Query("DELETE FROM breed")
    suspend fun clearAll()

}