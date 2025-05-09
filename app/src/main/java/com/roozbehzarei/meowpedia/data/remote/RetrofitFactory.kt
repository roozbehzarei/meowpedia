package com.roozbehzarei.meowpedia.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitFactory {

    private const val BASE_URL = "https://api.thecatapi.com/"

    fun create(client: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            isLenient = true
        }
        return Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(
            json.asConverterFactory(
                "application/json; charset=UTF8".toMediaType()
            )
        ).build()
    }

}