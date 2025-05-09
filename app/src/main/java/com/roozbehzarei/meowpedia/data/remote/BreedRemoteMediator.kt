package com.roozbehzarei.meowpedia.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.roozbehzarei.meowpedia.data.local.db.BreedDatabase
import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import com.roozbehzarei.meowpedia.data.local.entity.RemoteKeyEntity

private const val REMOTE_KEY_ID = "breed_remote_key"

@OptIn(ExperimentalPagingApi::class)
class BreedRemoteMediator(
    private val breedDatabase: BreedDatabase, private val breedApi: BreedApi
) : RemoteMediator<Int, BreedEntity>() {

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, BreedEntity>
    ): MediatorResult {
        return try {
            val pageToLoad = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {

                    val remoteKey = breedDatabase.remoteKeyDao().getById(REMOTE_KEY_ID)
                    if (remoteKey?.nextPage == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        remoteKey.nextPage
                    }
                }
            }

            val breeds = breedApi.getBreeds(pageSize = state.config.pageSize, page = pageToLoad)
            val nextPage = if (breeds.isEmpty()) null else pageToLoad + 1

            breedDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    breedDatabase.breedDao().clearAll()
                    breedDatabase.remoteKeyDao().deleteById(REMOTE_KEY_ID)
                }
                val breedEntities = breeds.map { breed -> breed.toBreedEntity(null) }
                breedDatabase.breedDao().upsertAll(breedEntities)

                breedDatabase.remoteKeyDao().insert(RemoteKeyEntity(REMOTE_KEY_ID, nextPage))
                MediatorResult.Success(breeds.isEmpty())
            }


        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }

}