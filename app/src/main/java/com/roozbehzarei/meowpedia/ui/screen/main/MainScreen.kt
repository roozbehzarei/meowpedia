package com.roozbehzarei.meowpedia.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.roozbehzarei.meowpedia.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onBreedClicked: (id: String) -> Unit,
    onShowMessage: (message: String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val favoriteItemIds = uiState.favoriteItems.filter { it.isFavorite == true }.map { it.id }
    val breeds = viewModel.breedPagingFlow.collectAsLazyPagingItems()
    var isManuallyRefreshingList by rememberSaveable { mutableStateOf(false) }
    var hasShownNetworkError by rememberSaveable { mutableStateOf(false) }
    var imageToLoad by remember { mutableStateOf("") }

    LaunchedEffect(lazyListState.isScrollInProgress) {
        focusManager.clearFocus()
    }

    LaunchedEffect(imageToLoad) {
        if (imageToLoad.isNotBlank()) {
            viewModel.getBreedImage(imageToLoad)
        }
    }

    LaunchedEffect(breeds.loadState) {
        if ((breeds.loadState.refresh is LoadState.Loading).not()) isManuallyRefreshingList = false
        if (breeds.loadState.refresh is LoadState.Error && hasShownNetworkError.not()) {
            onShowMessage("A network error has occurred")
            hasShownNetworkError = true
        }
    }

    Column(modifier = modifier) {
        BreedSearchField { input ->
            viewModel.setSearchQuery(input)
        }
        if (breeds.loadState.refresh is LoadState.Error && breeds.itemCount == 0) {
            NetworkError {
                breeds.refresh()
            }
        } else if (breeds.loadState.refresh is LoadState.Loading && breeds.itemCount == 0) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            PullToRefreshBox(
                isRefreshing = isManuallyRefreshingList,
                onRefresh = {
                    isManuallyRefreshingList = true
                    hasShownNetworkError = false
                    breeds.refresh()
                },
                modifier = modifier.padding(top = 16.dp),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = lazyListState
                ) {
                    if (uiState.isSearchMode) {
                        if (uiState.search.isLoading) {
                            item {
                                CircularProgressIndicator()
                            }
                        } else if (uiState.search.isLoading.not() && uiState.search.result.isEmpty()) item {
                            Text(
                                "Could not find any breeds matching your criteria.",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        else {
                            items(items = uiState.search.result, key = { breed ->
                                breed.id
                            }) { breed ->
                                BreedItem(
                                    modifier = modifier.padding(horizontal = 8.dp),
                                    id = breed.id,
                                    imageUrl = breed.imageUrl,
                                    name = breed.name,
                                    origin = breed.origin,
                                    temperament = breed.temperament,
                                    isFavorite = favoriteItemIds.contains(breed.id),
                                    onClick = { id -> onBreedClicked(id) },
                                    onFavoriteToggle = { id, isFavorite ->
                                        viewModel.updateFavoriteItem(id, isFavorite.not())
                                    })
                            }
                        }
                    } else {
                        items(breeds.itemCount) { index ->
                            breeds[index]?.let { breed ->
                                if (breed.imageId != null && breed.imageUrl == null) imageToLoad =
                                    breed.imageId
                                BreedItem(
                                    modifier = modifier.padding(horizontal = 8.dp),
                                    id = breed.id,
                                    imageUrl = breed.imageUrl,
                                    name = breed.name,
                                    origin = breed.origin,
                                    temperament = breed.temperament,
                                    isFavorite = favoriteItemIds.contains(breed.id),
                                    onClick = { id -> onBreedClicked(id) },
                                    onFavoriteToggle = { id, isFavorite ->
                                        viewModel.updateFavoriteItem(id, isFavorite.not())
                                    })
                            }
                        }
                    }
                }
            }
        }
    }


}

@Composable
private fun NetworkError(
    onRetryClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("There was an error fetching the list.", style = MaterialTheme.typography.titleMedium)
        Button(
            modifier = Modifier.padding(top = 8.dp), onClick = {
                onRetryClicked()
            }) {
            Text("Try again", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun BreedItem(
    modifier: Modifier = Modifier,
    id: String,
    imageUrl: String?,
    name: String,
    origin: String,
    temperament: String,
    isFavorite: Boolean,
    onClick: (id: String) -> Unit,
    onFavoriteToggle: (id: String, isFavorite: Boolean) -> Unit
) {
    val context = LocalContext.current

    Card(modifier = modifier, onClick = { onClick(id) }) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(start = 12.dp, top = 12.dp, end = 0.dp, bottom = 12.dp)
                    .heightIn(max = 72.dp)
                    .aspectRatio(1f)
                    .clip(
                        RoundedCornerShape(8.dp)
                    ),
                contentScale = ContentScale.Crop,
                model = ImageRequest.Builder(context).data(imageUrl).crossfade(true).build(),
                error = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
            Column(
                Modifier
                    .padding(12.dp)
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Origin: $origin",
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Temperament: $temperament",
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(modifier = Modifier.padding(8.dp), onClick = {
                onFavoriteToggle(id, isFavorite)
            }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun BreedSearchField(onInput: (input: String) -> Unit) {

    var query by remember { mutableStateOf("") }

    TextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = query,
        onValueChange = {
            query = it
            onInput(query)
        },
        label = { Text(text = " Search breed") },
        trailingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null)

        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(25.dp)
    )
}

@Preview
@Composable
private fun BreedSearchFieldPreview() {
    BreedSearchField {}
}

@Preview
@Composable
private fun BreedItemPreview() {
    BreedItem(
        modifier = Modifier.fillMaxWidth(),
        id = "",
        imageUrl = "",
        name = "American Curl",
        temperament = "Affectionate, Curious, Intelligent, Interactive, Lively, Playful, Social",
        origin = "United States",
        isFavorite = false,
        onClick = {},
        onFavoriteToggle = { _, _ -> })
}