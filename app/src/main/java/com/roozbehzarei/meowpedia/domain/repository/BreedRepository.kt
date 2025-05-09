package com.roozbehzarei.meowpedia.domain.repository

interface BreedRepository {
    suspend fun getImage(key: String)
}