package net.obsidianx.watchchime

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: ConfigScreenViewModel by viewModels {
            ConfigScreenViewModelFactory(
                SharedPrefsRepository(
                    getSharedPreferences(
                        PREFS_NAME,
                        MODE_PRIVATE
                    )
                )
            )
        }
        setContent {
            ConfigScreen(
                viewModel,
                onTestChime = {
                    sendBroadcast(Intent(applicationContext, ChimeReceiver::class.java))
                },
                onTestSchedule = {
                    ChimeScheduler.scheduleNext(applicationContext, true)
                    Toast.makeText(
                        applicationContext,
                        R.string.test_schedule_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onSetVolume = { viewModel.setVolume(it) },
                onToggleEnabled = viewModel::toggleEnabled,
                onToggleDisableWhenLocked = viewModel::toggleDisableWhenLocked,
            )
        }

        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.S..Build.VERSION_CODES.S_V2) {
            if (ChimeScheduler.needsExactAlarmPermission(this)) {
                startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                Log.w("Chime", "Missing permission. Skipping scheduling.")
                return
            }
        }

        ChimeScheduler.scheduleNext(this)
    }
}
