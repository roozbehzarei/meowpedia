package com.roozbehzarei.meowpedia.di

import com.roozbehzarei.meowpedia.data.remote.BreedApi
import com.roozbehzarei.meowpedia.data.remote.OkHttpClientFactory
import com.roozbehzarei.meowpedia.data.remote.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt module responsible for providing network-related dependencies.
 *
 * Installed in the [SingletonComponent], ensuring that provided instances
 * live as long as the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClientFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = RetrofitFactory.create(client)

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): BreedApi =
        retrofit.create(BreedApi::class.java)

}