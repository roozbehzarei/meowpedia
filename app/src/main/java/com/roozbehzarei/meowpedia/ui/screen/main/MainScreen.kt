package com.roozbehzarei.meowpedia.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    val breeds = viewModel.breedPagingFlow.collectAsLazyPagingItems()
    var isBreedsListRefreshing by rememberSaveable { mutableStateOf(false) }
    var hasShownNetworkError by rememberSaveable { mutableStateOf(false) }
    var imageToLoad by remember { mutableStateOf("") }

    LaunchedEffect(imageToLoad) {
        if (imageToLoad.isNotBlank()) {
            viewModel.getBreedImage(imageToLoad)
        }
    }

    LaunchedEffect(breeds.loadState) {
        isBreedsListRefreshing = when (breeds.loadState.refresh) {
            is LoadState.Loading -> true
            is LoadState.NotLoading -> false
            is LoadState.Error -> false
        }
        if (breeds.loadState.refresh is LoadState.Error && hasShownNetworkError.not()) {
            onShowMessage("A network error has occurred")
            hasShownNetworkError = true
        }
    }

    PullToRefreshBox(
        isRefreshing = isBreedsListRefreshing,
        onRefresh = {
            hasShownNetworkError = false
            breeds.refresh()
        },
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(breeds.itemCount) { index ->
                breeds[index]?.let { breed ->
                    if (breed.imageId != null && breed.imageUrl == null) imageToLoad = breed.imageId
                    BreedItem(
                        modifier = modifier.padding(horizontal = 8.dp),
                        id = breed.id,
                        imageUrl = breed.imageUrl,
                        name = breed.name,
                        origin = breed.origin,
                        temperament = breed.temperament,
                        isFavorite = false,
                        onClick = { id -> onBreedClicked(id) },
                    )
                }
            }
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
                // TODO
            }) {
                Icon(
                    if (isFavorite) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder, contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun BreedItemPreview() {
    BreedItem(
        modifier = Modifier.fillMaxSize(),
        id = "",
        imageUrl = "",
        name = "American Curl",
        temperament = "Affectionate, Curious, Intelligent, Interactive, Lively, Playful, Social",
        origin = "United States",
        isFavorite = false,
        onClick = {})
}