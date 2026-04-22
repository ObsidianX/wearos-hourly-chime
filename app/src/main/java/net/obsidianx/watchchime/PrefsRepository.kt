package net.obsidianx.watchchime

import android.content.SharedPreferences
import androidx.core.content.edit

interface PrefsRepository {
    var enabled: Boolean
    var disableWhenLocked: Boolean
    var volume: Int
}

class SharedPrefsRepository(private val sharedPreferences: SharedPreferences) : PrefsRepository {
    private var _enabled: Boolean = sharedPreferences.getBoolean(PREF_ENABLED, true)
    override var enabled: Boolean
        get() = _enabled
        set(value) {
            sharedPreferences.edit(commit = true) { putBoolean(PREF_ENABLED, value) }
            _enabled = value
        }

    private var _disableWhenLocked: Boolean =
        sharedPreferences.getBoolean(PREF_DISABLE_LOCKED, true)
    override var disableWhenLocked: Boolean
        get() = _disableWhenLocked
        set(value) {
            sharedPreferences.edit(commit = true) { putBoolean(PREF_DISABLE_LOCKED, value) }
            _disableWhenLocked = value
        }

    private var _volume: Int = sharedPreferences.getInt(PREF_VOLUME, 10)
    override var volume: Int
        get() = _volume
        set(value) {
            sharedPreferences.edit(commit = true) { putInt(PREF_VOLUME, value) }
            _volume = value
        }
}