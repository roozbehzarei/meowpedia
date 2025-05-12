package com.roozbehzarei.meowpedia.ui.screen.breed_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roozbehzarei.meowpedia.BuildConfig
import com.roozbehzarei.meowpedia.domain.model.Favorite
import com.roozbehzarei.meowpedia.domain.repository.BreedRepository
import com.roozbehzarei.meowpedia.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Breed Details screen.
 *
 * Handles loading breed data, fetching images, and managing favorite status.
 * Exposes a [StateFlow] of [BreedDetailsUiState] to drive the UI.
 */
@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    private val breedRepository: BreedRepository, private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BreedDetailsUiState(null, null, null))
    val uiState: StateFlow<BreedDetailsUiState> = _uiState.asStateFlow()

    // Job reference for the current favorite update operation, to allow cancellation
    var updateFavoriteJob: Job? = null

    /**
     * Loads breed details for the given [id] and updates UI state.
     * Collects from the repository's Flow to receive real-time updates.
     *
     * @param id Unique identifier of the breed to load
     */
    fun getBreedDetails(id: String) {
        viewModelScope.launch {
            val breedFlow = breedRepository.getBreedDetails(id)
            breedFlow.collect { breed ->
                _uiState.update { it.copy(breed = breed) }
            }
        }
    }

    /**
     * Fetches the breed image from the network and caches it.
     * Executes on [Dispatchers.IO] and updates local DB via repository.
     *
     * @param key Image identifier used by the API
     */
    fun getBreedImage(key: String) {
        viewModelScope.launch {
            with(Dispatchers.IO) {
                try {
                    breedRepository.getImage(key)
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) e.printStackTrace()
                }
            }
        }
    }

    /**
     * Retrieves the favorite status for the given breed [id] and updates UI state.
     *
     * @param id Unique breed identifier to check favorite status
     */
    fun getFavoriteItem(id: String) {
        viewModelScope.launch {
            val favoriteItem = favoriteRepository.getFavorite(id)
            _uiState.update { it.copy(isFavorite = favoriteItem?.isFavorite) }
        }
    }

    /**
     * Toggles or sets favorite status for a breed.
     *
     * Cancels any ongoing update job before launching a new one.
     *
     * Updates UI state with the new job for cancellation control.
     *
     * @param id Unique breed identifier
     * @param isFavorite `true` to mark as favorite, `false` to remove
     */
    fun updateFavoriteItem(id: String, isFavorite: Boolean) {
        updateFavoriteJob?.cancel()
        val favoriteItem = Favorite(id, isFavorite)
        updateFavoriteJob = viewModelScope.launch {
            favoriteRepository.updateFavorite(favoriteItem)
        }
        _uiState.update { it.copy(updateFavoriteJob = updateFavoriteJob) }
    }

}