package com.roozbehzarei.meowpedia.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BreedDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("temperament") val temperament: String,
    @SerialName("origin") val origin: String,
    @SerialName("description") val description: String,
    @SerialName("life_span") val lifeSpan: String,
    @SerialName("wikipedia_url") val wikipediaUrl: String? = null,
    @SerialName("reference_image_id") val imageId: String? = null,
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