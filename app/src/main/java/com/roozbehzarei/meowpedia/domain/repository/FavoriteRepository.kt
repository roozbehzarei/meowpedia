package com.roozbehzarei.meowpedia.domain.repository

import com.roozbehzarei.meowpedia.domain.model.Favorite
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing favorite cat breeds.
 *
 */
interface FavoriteRepository {
    suspend fun getAllFavorites(): Flow<List<Favorite>>
    suspend fun getFavorite(id: String): Favorite?
    suspend fun updateFavorite(favorite: Favorite)
}