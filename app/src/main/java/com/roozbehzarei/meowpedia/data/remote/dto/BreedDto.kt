package com.roozbehzarei.meowpedia.data.remote.dto

@com.squareup.moshi.JsonClass(generateAdapter = true)
data class BreedDto(
    @com.squareup.moshi.Json(name = "id") val id: String,
    @com.squareup.moshi.Json(name = "name") val name: String,
    @com.squareup.moshi.Json(name = "temperament") val temperament: String,
    @com.squareup.moshi.Json(name = "origin") val origin: String,
    @com.squareup.moshi.Json(name = "description") val description: String,
    @com.squareup.moshi.Json(name = "life_span") val lifeSpan: String,
    @com.squareup.moshi.Json(name = "wikipedia_url") val wikipediaUrl: String?,
    @com.squareup.moshi.Json(name = "reference_image_id") val imageId: String?,
) {
    fun toBreedEntity(imageUrl: String?) = com.roozbehzarei.meowpedia.data.local.entity.BreedEntity(
        id = id,
        name = name,
        temperament = temperament,
        origin = origin,
        description = description,
        lifeSpan = lifeSpan,
        wikipediaUrl = wikipediaUrl,
        imageId = imageId,
        imageUrl = imageUrl
    )
}