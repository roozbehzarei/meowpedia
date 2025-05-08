package com.roozbehzarei.meowpedia.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.roozbehzarei.meowpedia.data.remote.BreedRemoteMediator
import com.roozbehzarei.meowpedia.data.local.db.BreedDatabase
import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import com.roozbehzarei.meowpedia.data.remote.BreedApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object PagerModule {

    @Provides
    @Singleton
    fun provideBreedPager(breedApi: BreedApi, breedDb: BreedDatabase): Pager<Int, BreedEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10), remoteMediator = BreedRemoteMediator(
                breedApi = breedApi, breedDatabase = breedDb
            ), pagingSourceFactory = { breedDb.breedDao().pagingSource() })
    }

}