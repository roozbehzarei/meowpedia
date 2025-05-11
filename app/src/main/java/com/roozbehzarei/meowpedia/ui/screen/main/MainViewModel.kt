package com.roozbehzarei.meowpedia.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.roozbehzarei.meowpedia.BuildConfig
import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import com.roozbehzarei.meowpedia.domain.model.Favorite
import com.roozbehzarei.meowpedia.domain.model.Search
import com.roozbehzarei.meowpedia.domain.repository.BreedRepository
import com.roozbehzarei.meowpedia.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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

    private val _uiState = MutableStateFlow(
        MainUiState(
            favoriteItems = listOf(), isSearchMode = false, search = Search(false, listOf())
        )
    )
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    private val searchQuery = MutableStateFlow<String>("")
    val breedPagingFlow = pager.flow.map { pagingData ->
        pagingData.map { it.toBreed() }
    }.cachedIn(viewModelScope)

    init {
        getFavorites()
        fetchSearchResult()
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
            }
        }
    }

    fun updateFavoriteItem(id: String, isFavorite: Boolean) {
        val favoriteItem = Favorite(id, isFavorite)
        viewModelScope.launch {
            favoriteRepository.updateFavorite(favoriteItem)
        }
    }

    fun setSearchQuery(input: String) {
        searchQuery.value = input
        _uiState.update {
            it.copy(
                isSearchMode = input.isNotBlank(),
                search = Search(isLoading = input.isNotBlank(), result = listOf())
            )
        }
    }

    @OptIn(FlowPreview::class)
    fun fetchSearchResult() {
        viewModelScope.launch {
            searchQuery.debounce(500).distinctUntilChanged().collect {
                if (it.isNotBlank()) {
                    val breeds = breedRepository.searchBreeds(it)
                    _uiState.update { it.copy(search = Search(isLoading = false, result = breeds)) }
                }
            }
        }
    }

}