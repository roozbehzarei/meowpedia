package com.roozbehzarei.meowpedia.ui.screen.breed_details

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.roozbehzarei.meowpedia.BuildConfig
import com.roozbehzarei.meowpedia.R

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun BreedDetailsScreen(
    modifier: Modifier = Modifier, id: String, viewModel: BreedDetailsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(Unit) {
        viewModel.getBreedDetails(id)
        viewModel.getFavoriteItem(id)
    }

    LaunchedEffect(uiState.breed) {
        if (uiState.breed?.imageId != null && uiState.breed?.imageUrl == null) {
            viewModel.getBreedImage(uiState.breed!!.imageId!!)
        }
    }

    LaunchedEffect(viewModel.updateFavoriteJob) {
        if (viewModel.updateFavoriteJob?.isCompleted == true) viewModel.getFavoriteItem(id)
    }

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight / 3)
                .clip(MaterialTheme.shapes.large),
            contentScale = ContentScale.Crop,
            model = ImageRequest.Builder(context).data(uiState.breed?.imageUrl).crossfade(true)
                .build(),
            contentDescription = null
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = uiState.breed?.name.orEmpty(),
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(onClick = {
                if (uiState.isFavorite == true) {
                    viewModel.updateFavoriteItem(id, false)
                } else {
                    viewModel.updateFavoriteItem(id, true)
                }
            }) {
                Icon(
                    imageVector = if (uiState.isFavorite == true) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (uiState.isFavorite == true) Color.Red
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.lifespan),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.origin),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.year_unit, uiState.breed?.lifeSpan.orEmpty()),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(1f),
                text = uiState.breed?.origin.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp), onClick = {
                uiState.breed?.wikipediaUrl?.let { launchUrl(context, it) }
            }) {
            Text(
                text = stringResource(R.string.wikipedia_button),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(8.dp))
        Card(
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = uiState.breed?.description.orEmpty(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}

private fun launchUrl(context: Context, url: String) {
    val intent = CustomTabsIntent.Builder().build()
    try {
        intent.launchUrl(context, url.toUri())
    } catch (e: Exception) {
        Toast.makeText(context, context.getString(R.string.link_error), Toast.LENGTH_SHORT).show()
        if (BuildConfig.DEBUG) e.printStackTrace()
    }

}