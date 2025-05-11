package com.roozbehzarei.meowpedia.ui.screen.breed_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roozbehzarei.meowpedia.BuildConfig
import com.roozbehzarei.meowpedia.domain.model.Favorite
import com.roozbehzarei.meowpedia.domain.repository.BreedRepository
import com.roozbehzarei.meowpedia.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    private val breedRepository: BreedRepository, private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BreedDetailsUiState(null, null, null))
    val uiState: StateFlow<BreedDetailsUiState> = _uiState.asStateFlow()
    var updateFavoriteJob: Job? = null

    fun getBreedDetails(id: String) {
        viewModelScope.launch {
            try {
                val breed = breedRepository.getBreedDetails(id)
                _uiState.update { it.copy(breed = breed) }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) e.printStackTrace()
            }
        }
    }

    fun getFavoriteItem(id: String) {
        viewModelScope.launch {
            val favoriteItem = favoriteRepository.getFavorite(id)
            _uiState.update { it.copy(isFavorite = favoriteItem?.isFavorite) }
        }
    }

    fun updateFavoriteItem(id: String, isFavorite: Boolean) {
        updateFavoriteJob?.cancel()
        val favoriteItem = Favorite(id, isFavorite)
        updateFavoriteJob = viewModelScope.launch {
            favoriteRepository.updateFavorite(favoriteItem)
        }
        _uiState.update { it.copy(updateFavoriteJob = updateFavoriteJob) }
    }

}