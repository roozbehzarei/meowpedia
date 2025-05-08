package com.roozbehzarei.meowpedia.data.local.entity

@androidx.room.Entity(tableName = "breed")
data class BreedEntity(
    @androidx.room.PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val temperament: String,
    val origin: String,
    val description: String,
    @androidx.room.ColumnInfo(name = "life_span") val lifeSpan: String,
    @androidx.room.ColumnInfo(name = "wikipedia_url") val wikipediaUrl: String?,
    @androidx.room.ColumnInfo(name = "reference_image_id") val imageId: String?,
    @androidx.room.ColumnInfo(name = "image_id") val imageUrl: String?,
) {
    fun toBreed() = com.roozbehzarei.meowpedia.domain.model.Breed(
        id = id,
        name = name,
        temperament = temperament,
        origin = origin,
        description = description,
        lifeSpan = lifeSpan,
        wikipediaUrl = wikipediaUrl,
        imageUrl = imageUrl
    )
}