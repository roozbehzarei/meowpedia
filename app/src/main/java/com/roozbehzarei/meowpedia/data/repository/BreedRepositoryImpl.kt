package com.roozbehzarei.meowpedia.data.repository

import com.roozbehzarei.meowpedia.BuildConfig
import com.roozbehzarei.meowpedia.data.local.dao.BreedDao
import com.roozbehzarei.meowpedia.data.mapper.toBreed
import com.roozbehzarei.meowpedia.data.remote.BreedApi
import com.roozbehzarei.meowpedia.domain.model.Breed
import com.roozbehzarei.meowpedia.domain.repository.BreedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BreedRepositoryImpl @Inject constructor(
    private val breedApi: BreedApi, private val breedDao: BreedDao
) : BreedRepository {

    /**
     * Fetches the image associated with the given [key] (image id) from the network,
     * and updates the locally cached BreedEntity with the new image URL.
     *
     * @param key Unique identifier of the breed image
     */
    override suspend fun getImage(key: String) {
        val image = breedApi.getBreedImage(key)
        image.url?.let { url ->
            breedDao.updateImage(image.id, url)
        }
    }

    /**
     * Returns a [Flow] emitting breed details for the given [id].
     * The flow automatically emits updates whenever the local database entry changes.
     *
     * @param id Unique identifier of the breed
     * @return Flow stream of [Breed] domain models
     */
    override suspend fun getBreedDetails(id: String): Flow<Breed> {
        return breedDao.getById(id).map { it.toBreed() }
    }

    /**
     * Searches for breeds matching the [name].
     *
     * Performs a network search to fetch the latest breed data,
     * updates or inserts entries in the local database, and then
     * returns the cached results filtered by [name].
     *
     * In case of network errors, it falls back to the local search results.
     *
     * @param name Text to filter breeds by name
     * @return List of [Breed] domain models matching the query
     */
    override suspend fun searchBreeds(name: String): List<Breed> {
        try {
            // Fetch remote breed list matching the query
            val fetchedBreeds = breedApi.searchBreeds(name)
            fetchedBreeds.forEach { breed ->
                // Check if the breed already exists locally
                val existingBreedEntity = breedDao.getByIdOrNull(breed.id)
                if (existingBreedEntity == null) {
                    // Insert new breed entity if not present
                    breedDao.insert(breed.toBreedEntity(null))
                } else {
                    // Update fields of existing entity
                    breedDao.update(
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
                // If a new imageId exists and no URL is cached, fetch and update
                if (breed.imageId != null && existingBreedEntity?.imageUrl == null) {
                    val breedImageUrl = breedApi.getBreedImage(breed.imageId)
                    breedDao.updateImage(breedImageUrl.id, breedImageUrl.url!!)
                }
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }
        // Always return local search results; updated or cached
        return breedDao.searchBreeds(name).map { it.toBreed() }
    }

}