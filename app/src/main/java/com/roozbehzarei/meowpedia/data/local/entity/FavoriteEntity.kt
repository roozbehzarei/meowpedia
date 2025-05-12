package com.roozbehzarei.meowpedia.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a favorite state for a cat breed in the local database.
 *
 * @property id The unique identifier of the cat breed. This serves as the primary key.
 * @property isFavorite A boolean value indicating whether the cat breed is favorited (true) or not (false), or null if the state is not explicitly set.
 */
@Entity(tableName = "favorite")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean?
)