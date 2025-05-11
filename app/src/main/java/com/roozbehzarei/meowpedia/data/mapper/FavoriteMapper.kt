package com.roozbehzarei.meowpedia.data.mapper

import com.roozbehzarei.meowpedia.data.local.entity.FavoriteEntity
import com.roozbehzarei.meowpedia.domain.model.Favorite

fun FavoriteEntity.toFavorite(): Favorite = Favorite(id, isFavorite)

fun Favorite.toEntity(): FavoriteEntity = FavoriteEntity(id, isFavorite)