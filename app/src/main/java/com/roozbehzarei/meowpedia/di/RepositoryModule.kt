package com.roozbehzarei.meowpedia.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.roozbehzarei.meowpedia.data.local.dao.BreedDao
import com.roozbehzarei.meowpedia.data.local.dao.FavoriteDao
import com.roozbehzarei.meowpedia.data.local.db.BreedDatabase
import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import com.roozbehzarei.meowpedia.data.remote.BreedApi
import com.roozbehzarei.meowpedia.data.repository.BreedRemoteMediator
import com.roozbehzarei.meowpedia.data.repository.BreedRepositoryImpl
import com.roozbehzarei.meowpedia.data.repository.FavoriteRepositoryImpl
import com.roozbehzarei.meowpedia.domain.repository.BreedRepository
import com.roozbehzarei.meowpedia.domain.repository.FavoriteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing repository and paging dependencies.
 *
 * Installed in [SingletonComponent] for application-wide singletons.
 */
@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides a [Pager] that loads [BreedEntity] pages from network and database.
     *
     * Uses [BreedRemoteMediator] to synchronize remote data and local cache,
     * and a Room paging source for local queries.
     *
     * @param breedApi Retrofit interface for breed endpoints
     * @param breedDb Room database instance containing breed DAO
     * @return configured Pager for paging breed entities
     */
    @Provides
    @Singleton
    fun provideBreedPager(breedApi: BreedApi, breedDb: BreedDatabase): Pager<Int, BreedEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10), remoteMediator = BreedRemoteMediator(
                breedApi = breedApi, breedDatabase = breedDb
            ), pagingSourceFactory = { breedDb.breedDao().pagingSource() })
    }

    /**
     * Provides the [BreedRepository] implementation backed by network and local cache.
     *
     * @param breedApi Retrofit API for fetching breed data
     * @param breedDao Data Access Object for accessing/modifying Breed entities
     * @return an instance of [BreedRepositoryImpl]
     */
    @Provides
    @Singleton
    fun provideBreedRepository(breedApi: BreedApi, breedDao: BreedDao): BreedRepository {
        return BreedRepositoryImpl(breedApi, breedDao)
    }

    /**
     * Provides the [FavoriteRepository] for managing user's favorite breeds.
     *
     * @param favoriteDao Data Access Object for accessing/modifying favorite entities
     * @return an instance of [FavoriteRepositoryImpl]
     */
    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteDao: FavoriteDao): FavoriteRepository {
        return FavoriteRepositoryImpl(favoriteDao)
    }

}