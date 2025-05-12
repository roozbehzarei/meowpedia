package com.roozbehzarei.meowpedia.data.repository

import com.roozbehzarei.meowpedia.data.local.dao.FavoriteDao
import com.roozbehzarei.meowpedia.data.mapper.toEntity
import com.roozbehzarei.meowpedia.data.mapper.toFavorite
import com.roozbehzarei.meowpedia.domain.model.Favorite
import com.roozbehzarei.meowpedia.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the [FavoriteRepository] interface.
 *
 * This class provides methods to interact with favorite breed data stored in the local database.
 *
 * @property favoriteDao The Data Access Object used to access favorite breed.
 */
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override suspend fun getAllFavorites(): Flow<List<Favorite>> {
        return favoriteDao.getAll().map { entities ->
            entities.map { it.toFavorite() }
        }
    }

    override suspend fun getFavorite(id: String): Favorite? {
        return favoriteDao.getByIdOrNull(id)?.toFavorite()
    }

    override suspend fun updateFavorite(favorite: Favorite) {
        favoriteDao.upsert(favorite.toEntity())
    }

}