package com.roozbehzarei.meowpedia.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a remote key entity for managing pagination in the database.
 *
 * @property id The unique identifier for the remote key. This is typically used to associate the key with a specific query or dataset.
 * @property nextPage The page number to fetch next. A null value indicates that there are no more pages.
 */
@Entity(tableName = "remote_key")
data class RemoteKeyEntity(
    @PrimaryKey(autoGenerate = false) val id: String, val nextPage: Int?
)