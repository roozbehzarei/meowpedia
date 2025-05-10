package com.roozbehzarei.meowpedia.data.repository

import com.roozbehzarei.meowpedia.data.local.db.BreedDatabase
import com.roozbehzarei.meowpedia.data.remote.BreedApi
import com.roozbehzarei.meowpedia.domain.model.Breed
import com.roozbehzarei.meowpedia.domain.repository.BreedRepository
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

    override suspend fun getBreedDetails(id: String): Breed {
        return breedDatabase.breedDao().getById(id).toBreed()
    }

}