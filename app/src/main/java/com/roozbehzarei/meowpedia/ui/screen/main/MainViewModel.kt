package com.roozbehzarei.meowpedia.ui.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.roozbehzarei.meowpedia.BuildConfig
import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import com.roozbehzarei.meowpedia.domain.model.Favorite
import com.roozbehzarei.meowpedia.domain.repository.BreedRepository
import com.roozbehzarei.meowpedia.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    pager: Pager<Int, BreedEntity>,
    private val breedRepository: BreedRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState(listOf()))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    val breedPagingFlow = pager.flow.map { pagingData ->
        pagingData.map { it.toBreed() }
    }.cachedIn(viewModelScope)

    init {
        getFavorites()
    }

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

    private fun getFavorites() {
        viewModelScope.launch {
            val favorites = favoriteRepository.getAllFavorites()
            favorites.collect { favorite ->
                _uiState.update {
                    it.copy(favoriteItems = favorite)
                }
                Log.d("ROUZ", uiState.value.favoriteItems.size.toString())
            }
        }
    }

    fun updateFavoriteItem(id: String, isFavorite: Boolean) {
        val favoriteItem = Favorite(id, isFavorite)
        viewModelScope.launch {
            favoriteRepository.updateFavorite(favoriteItem)
        }
    }

}