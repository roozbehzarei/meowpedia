package com.roozbehzarei.meowpedia.data.repository

import com.roozbehzarei.meowpedia.data.local.db.BreedDatabase
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
 * @property breedDatabase The database instance used to access favorite breed.
 */
class FavoriteRepositoryImpl @Inject constructor(
    private val breedDatabase: BreedDatabase
) : FavoriteRepository {

    override suspend fun getAllFavorites(): Flow<List<Favorite>> {
        return breedDatabase.favoriteDao().getAll().map { entities ->
            entities.map { it.toFavorite() }
        }
    }

    override suspend fun getFavorite(id: String): Favorite? {
        return breedDatabase.favoriteDao().getByIdOrNull(id)?.toFavorite()
    }

    override suspend fun updateFavorite(favorite: Favorite) {
        breedDatabase.favoriteDao().upsert(favorite.toEntity())
    }

}