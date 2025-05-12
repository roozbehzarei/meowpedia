package com.roozbehzarei.meowpedia.data.mapper

import com.roozbehzarei.meowpedia.data.local.entity.FavoriteEntity
import com.roozbehzarei.meowpedia.domain.model.Favorite

/**
 * Converts a [FavoriteEntity] object to a [Favorite] object.
 * [Favorite] is the domain object representing a favorite breed in the application's business logic.
 *
 * @return The converted [Favorite] object.
 */
fun FavoriteEntity.toFavorite(): Favorite = Favorite(id, isFavorite)

/**
 * Converts a [Favorite] domain model to a [FavoriteEntity] data entity.
 *
 * @return The corresponding [FavoriteEntity] data entity.
 */
fun Favorite.toEntity(): FavoriteEntity = FavoriteEntity(id, isFavorite)