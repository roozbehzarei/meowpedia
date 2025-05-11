package com.roozbehzarei.meowpedia.data.repository

import com.roozbehzarei.meowpedia.BuildConfig
import com.roozbehzarei.meowpedia.data.local.db.BreedDatabase
import com.roozbehzarei.meowpedia.data.remote.BreedApi
import com.roozbehzarei.meowpedia.domain.model.Breed
import com.roozbehzarei.meowpedia.domain.repository.BreedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BreedRepositoryImpl @Inject constructor(
    private val breedApi: BreedApi, private val breedDatabase: BreedDatabase
) : BreedRepository {

    override suspend fun getImage(key: String) {
        val image = breedApi.getBreedImage(key)
        image.url?.let { url ->
            breedDatabase.breedDao().updateImage(image.id, url)
        }
    }

    override suspend fun getBreedDetails(id: String): Flow<Breed> {
        return breedDatabase.breedDao().getById(id).map { it.toBreed() }
    }

    override suspend fun searchBreeds(query: String): List<Breed> {
        try {
            val fetchedBreeds = breedApi.searchBreeds(query)
            fetchedBreeds.forEach { breed ->
                val existingBreedEntity = breedDatabase.breedDao().getByIdOrNull(breed.id)
                if (existingBreedEntity == null) {
                    breedDatabase.breedDao().insert(breed.toBreedEntity(null))
                } else {
                    breedDatabase.breedDao().update(
                        id = breed.id,
                        name = breed.name,
                        temperament = breed.temperament,
                        origin = breed.origin,
                        description = breed.description,
                        lifeSpan = breed.lifeSpan,
                        wikipediaUrl = breed.wikipediaUrl,
                        imageId = breed.imageId
                    )
                }
                if (breed.imageId != null && existingBreedEntity?.imageUrl == null) {
                    val breedImageUrl = breedApi.getBreedImage(breed.imageId)
                    breedDatabase.breedDao().updateImage(breedImageUrl.id, breedImageUrl.url!!)
                }
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }
        val x = breedDatabase.breedDao().searchBreeds(query).map { it.toBreed() }
        return x
    }

}