package com.roozbehzarei.meowpedia.ui.screen.breed_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roozbehzarei.meowpedia.BuildConfig
import com.roozbehzarei.meowpedia.domain.repository.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    private val breedRepository: BreedRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BreedDetailsUiState(null))
    val uiState: StateFlow<BreedDetailsUiState> = _uiState.asStateFlow()

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

}