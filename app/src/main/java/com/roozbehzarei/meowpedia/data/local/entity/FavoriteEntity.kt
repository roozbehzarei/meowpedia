package com.roozbehzarei.meowpedia.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean?
)