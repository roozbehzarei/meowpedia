package com.roozbehzarei.meowpedia.data.remote

import com.roozbehzarei.meowpedia.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient

object OkHttpClientFactory {

    private const val API_KEY = BuildConfig.CAT_API_KEY

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder().addHeader("x-api-key", API_KEY).build()
        chain.proceed(newRequest)
    }

    fun create(): OkHttpClient {
        val client = OkHttpClient.Builder().addInterceptor(authInterceptor).build()
        return client
    }

}