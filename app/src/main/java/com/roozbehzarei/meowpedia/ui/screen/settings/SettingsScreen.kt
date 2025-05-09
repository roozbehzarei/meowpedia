package com.roozbehzarei.meowpedia.ui.screen.settings

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.roozbehzarei.meowpedia.R

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isDynamicColor by viewModel.isDynamicColor.collectAsState(false)
    val themeModeIndex by viewModel.themeMode.collectAsState(1)
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        SettingsItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .defaultMinSize(minHeight = 64.dp),
            title = stringResource(R.string.theme),
            description = when (themeModeIndex) {
                0 -> stringResource(R.string.mode_light)
                2 -> stringResource(R.string.mode_dark)
                else -> stringResource(R.string.mode_default)
            },
            descriptionMaxLines = 1,
            icon = painterResource(R.drawable.outline_palette),
            options = { modifier ->
                val options = listOf(
                    painterResource(R.drawable.outline_light_mode),
                    painterResource(R.drawable.outline_contrast),
                    painterResource(R.drawable.outline_dark_mode)
                )
                SingleChoiceSegmentedButtonRow(modifier = modifier) {
                    options.forEachIndexed { index, icon ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                            index = index, count = options.size
                        ), onClick = {
                            viewModel.saveThemeModePreference(index)
                        }, selected = index == themeModeIndex, label = { Icon(icon, null) })
                    }
                }
            })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SettingsItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .defaultMinSize(minHeight = 64.dp),
                title = stringResource(R.string.dynamic_color),
                description = stringResource(R.string.dynamic_color_desc),
                descriptionMaxLines = 2,
                icon = painterResource(R.drawable.outline_palette),
                options = { modifier ->
                    Switch(
                        modifier = modifier, checked = isDynamicColor, onCheckedChange = {
                            viewModel.saveDynamicColorsPreference(it)
                        })
                })
        }
    }
}

@Composable
private fun SettingsItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    descriptionMaxLines: Int,
    icon: Painter,
    options: (@Composable (modifier2: Modifier) -> Unit)?
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(horizontal = 18.dp),
            painter = icon,
            contentDescription = null
        )
        Column(Modifier.weight(1f)) {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                description,
                maxLines = descriptionMaxLines,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
        }
        if (options != null) options(Modifier.padding(horizontal = 18.dp))
    }
}