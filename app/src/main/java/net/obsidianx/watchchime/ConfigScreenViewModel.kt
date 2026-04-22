package net.obsidianx.watchchime

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ConfigScreenState(
    val enabled: Boolean = true,
    val volume: Int = 10,
    val disableWhenLocked: Boolean = true,
)

class ConfigScreenViewModelProvider : PreviewParameterProvider<ConfigScreenViewModel> {
    override val values: Sequence<ConfigScreenViewModel>
        get() = sequenceOf(ConfigScreenViewModel(object : PrefsRepository {
            override var enabled: Boolean = true
            override var disableWhenLocked: Boolean = true
            override var volume: Int = 50
        }))
}

class ConfigScreenViewModelFactory(private val prefsRepo: PrefsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfigScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConfigScreenViewModel(prefsRepo) as T
        }
        return super.create(modelClass)
    }
}

class ConfigScreenViewModel(private val prefs: PrefsRepository) : ViewModel() {
    private val _state = MutableStateFlow(
        ConfigScreenState(
            enabled = prefs.enabled,
            volume = prefs.volume,
            disableWhenLocked = prefs.disableWhenLocked,
        )
    )
    val state = _state.asStateFlow()

    fun toggleEnabled() {
        _state.update { curr ->
            prefs.enabled = !curr.enabled
            curr.copy(enabled = prefs.enabled)
        }
    }

    fun toggleDisableWhenLocked() {
        _state.update { curr ->
            prefs.disableWhenLocked = !curr.disableWhenLocked
            curr.copy(disableWhenLocked = prefs.disableWhenLocked)
        }
    }

    fun setVolume(volume: Int) {
        _state.update { curr ->
            prefs.volume = volume
            curr.copy(volume = volume)
        }
    }
}