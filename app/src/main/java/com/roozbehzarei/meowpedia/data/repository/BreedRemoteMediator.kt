package com.roozbehzarei.meowpedia.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.roozbehzarei.meowpedia.data.local.db.BreedDatabase
import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import com.roozbehzarei.meowpedia.data.local.entity.RemoteKeyEntity
import com.roozbehzarei.meowpedia.data.remote.BreedApi

private const val REMOTE_KEY_ID = "breed_remote_key"

/**
 * RemoteMediator orchestrating data sync between the network and Room cache for breeds paging.
 *
 * Follows an offline-first approach:
 * - On [LoadType.REFRESH], clears local cache and fetches first page.
 * - On [LoadType.APPEND], uses stored [RemoteKeyEntity] to determine next page or
 *   ends pagination when no further pages exist.
 * - [LoadType.PREPEND] is unsupported and treated as end of pagination.
 *
 * Stores next-page information in [RemoteKeyEntity] with a fixed key id.
 *
 * Executes all database writes in a transaction to ensure atomic updates.
 */
@OptIn(ExperimentalPagingApi::class)
class BreedRemoteMediator(
    private val breedDatabase: BreedDatabase, private val breedApi: BreedApi
) : RemoteMediator<Int, BreedEntity>() {

    /**
     * Loads a page of [BreedEntity] based on [loadType] and [state] configuration.
     *
     * @param loadType Type of load operation (REFRESH, PREPEND, APPEND)
     * @param state Paging state containing page size and loaded pages
     * @return `MediatorResult` indicating success or error and end-of-pagination state
     */
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, BreedEntity>
    ): MediatorResult {
        return try {
            // Determine which page to load
            val pageToLoad = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    // Retrieve stored nextPage from remote keys
                    val remoteKey = breedDatabase.remoteKeyDao().getById(REMOTE_KEY_ID)
                    // If no nextPage, end pagination
                    if (remoteKey?.nextPage == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        remoteKey.nextPage
                    }
                }
            }
            // Fetch breeds page from network
            val breeds = breedApi.getBreeds(pageSize = state.config.pageSize, page = pageToLoad)
            val nextPage = if (breeds.isEmpty()) null else pageToLoad + 1
            // Persist network and remote key data in a transaction
            breedDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // On refresh, clear existing data and keys
                    breedDatabase.breedDao().clearAll()
                    breedDatabase.remoteKeyDao().deleteById(REMOTE_KEY_ID)
                }
                // Map network models to entities and upsert
                val breedEntities = breeds.map { breed -> breed.toBreedEntity(null) }
                breedDatabase.breedDao().upsertAll(breedEntities)
                // Store or update remote key for next page
                breedDatabase.remoteKeyDao().insert(RemoteKeyEntity(REMOTE_KEY_ID, nextPage))
                // Signal success; if breeds list is empty then end of pagination
                MediatorResult.Success(breeds.isEmpty())
            }


        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }

}