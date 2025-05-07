package com.roozbehzarei.meowpedia.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breed")
data class Breed(
    @PrimaryKey val id: String,
    val name: String,
    val temperament: String,
    val origin: String,
    val description: String,
    @ColumnInfo(name = "life_span") val lifeSpan: String,
    @ColumnInfo(name = "wikipedia_url") val wikipediaUrl: String,
    @ColumnInfo(name = "reference_image_id") val imageId: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false
)