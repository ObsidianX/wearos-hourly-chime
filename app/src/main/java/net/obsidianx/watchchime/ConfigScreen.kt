package net.obsidianx.watchchime

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Slider
import androidx.wear.compose.material3.SwitchButton
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.dynamicColorScheme

@Composable
@Preview
fun ConfigScreen(
    @PreviewParameter(ConfigScreenViewModelProvider::class)
    viewModel: ConfigScreenViewModel,
    onTestChime: () -> Unit = {},
    onTestSchedule: () -> Unit = {},
    onSetVolume: (Int) -> Unit = {},
    onToggleEnabled: () -> Unit = {},
    onToggleDisableWhenLocked: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val columnState = rememberTransformingLazyColumnState()
    val context = LocalContext.current
    val dynamicColorScheme = dynamicColorScheme(context)
    val colors = dynamicColorScheme ?: MaterialTheme.colorScheme

    MaterialTheme(colorScheme = colors) {
        ScreenScaffold(scrollState = columnState) { contentPadding ->
            TransformingLazyColumn(
                state = columnState,
                contentPadding = contentPadding,
            ) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            stringResource(R.string.app_name),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            stringResource(R.string.app_desc),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                item {
                    SwitchButton(
                        modifier = Modifier.fillMaxWidth(),
                        checked = state.enabled,
                        onCheckedChange = { onToggleEnabled() }
                    ) {
                        Text(stringResource(R.string.option_enable))
                    }
                }

                item {
                    SwitchButton(
                        modifier = Modifier.fillMaxWidth(),
                        checked = state.disableWhenLocked,
                        onCheckedChange = { onToggleDisableWhenLocked() }
                    ) {
                        Text(stringResource(R.string.option_disable_locked))
                    }
                }

                item {
                    Column {
                        Text(
                            stringResource(R.string.volume, state.volume),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Slider(
                            state.volume.toFloat(),
                            onValueChange = { value ->
                                onSetVolume(value.toInt())
                            },
                            steps = 8,
                            valueRange = 10f..100f
                        )
                    }
                }

                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onTestChime,
                    ) {
                        Text(
                            stringResource(R.string.test_chime),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onTestSchedule,
                    ) {
                        Text(
                            stringResource(R.string.test_schedule),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}