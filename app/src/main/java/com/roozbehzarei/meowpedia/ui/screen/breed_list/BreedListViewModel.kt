package com.roozbehzarei.meowpedia.ui.screen.breed_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.roozbehzarei.meowpedia.BuildConfig
import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import com.roozbehzarei.meowpedia.data.mapper.toBreed
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

/**
 * ViewModel for the breed list screen, handling paging, search, and favorites.
 *
 * @property pager Paging source for breed entities, provided by Hilt.
 * @property breedRepository Repository for fetching breed data and images.
 * @property favoriteRepository Repository for managing favorite breeds.
 */
@HiltViewModel
class BreedListViewModel @Inject constructor(
    pager: Pager<Int, BreedEntity>,
    private val breedRepository: BreedRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        BreedListUiState(
            favoriteItems = listOf(), isSearchMode = false, search = Search(false, listOf())
        )
    )
    val uiState: StateFlow<BreedListUiState> = _uiState.asStateFlow()
    // Track the current search query text
    private val searchQuery = MutableStateFlow<String>("")
    // Flow of paged breed data mapped to domain models and cached in ViewModel scope
    val breedPagingFlow = pager.flow.map { pagingData ->
        pagingData.map { it.toBreed() }
    }.cachedIn(viewModelScope)

    init {
        // Fetch favorite breeds
        getFavorites()
        // Initialize search result observation
        fetchSearchResult()
    }

    /**
     * Fetches and caches an image for the given breed [key] asynchronously.
     *
     * @param key Identifier for the breed image.
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
     * Collects all favorite items from repository and updates UI state.
     */
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

    /**
     * Updates favorite status for a breed.
     *
     * @param id Unique identifier of the breed.
     * @param isFavorite `true` to mark as favorite, `false` to unfavorite.
     */
    fun updateFavoriteItem(id: String, isFavorite: Boolean) {
        val favoriteItem = Favorite(id, isFavorite)
        viewModelScope.launch {
            favoriteRepository.updateFavorite(favoriteItem)
        }
    }

    /**
     * Sets the current search query.
     *
     * Also activates search mode and sets search status to `isLoading` if input is not blank.
     *
     * @param input Text input for search query.
     */
    fun setSearchQuery(input: String) {
        searchQuery.value = input
        _uiState.update {
            it.copy(
                isSearchMode = input.isNotBlank(),
                search = Search(isLoading = input.isNotBlank(), result = listOf())
            )
        }
    }

    /**
     * Observes search query changes, applies a 500ms debounce to limit unnecessary calls,
     * ignores consecutive identical inputs, and performs the search when the query is non-blank.
     *
     * Upon receiving results, updates the UI state to reflect the search completion and display results.
     *
     * @see BreedRepository.searchBreeds
     */
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