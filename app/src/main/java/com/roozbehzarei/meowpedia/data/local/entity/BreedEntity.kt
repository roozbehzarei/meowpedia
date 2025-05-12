package com.roozbehzarei.meowpedia.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a breed entity stored in the local database.
 * This data class is mapped to a database table named "breed".
 *
 * @property id The unique identifier for the breed. This is the primary key.
 * @property name The name of the breed.
 * @property temperament The typical temperament of the breed.
 * @property origin The country or region of origin of the breed.
 * @property description A description of the breed.
 * @property lifeSpan The average lifespan of the breed.
 * @property wikipediaUrl The URL to the Wikipedia page for the breed (nullable).
 * @property imageId The reference ID for an image of the breed (nullable).
 * @property imageUrl The URL of an image for the breed (nullable).
 */
@Entity(tableName = "breed")
data class BreedEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val temperament: String,
    val origin: String,
    val description: String,
    @ColumnInfo(name = "life_span") val lifeSpan: String,
    @ColumnInfo(name = "wikipedia_url") val wikipediaUrl: String?,
    @ColumnInfo(name = "reference_image_id") val imageId: String?,
    @ColumnInfo(name = "image_id") val imageUrl: String?,
)
