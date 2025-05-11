package com.roozbehzarei.meowpedia.data.remote

import com.roozbehzarei.meowpedia.data.remote.dto.BreedDto
import com.roozbehzarei.meowpedia.data.remote.dto.BreedImageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BreedApi {

    @GET("v1/breeds")
    suspend fun getBreeds(
        @Query("limit") pageSize: Int, @Query("page") page: Int
    ): List<BreedDto>

    @GET("v1/images/{key}")
    suspend fun getBreedImage(
        @Path("key") imageKey: String
    ): BreedImageDto

    @GET("v1/breeds/search")
    suspend fun searchBreeds(
        @Query("q") query: String
    ): List<BreedDto>

}