package net.obsidianx.watchchime

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.PowerManager
import android.util.Log

class ChimeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pm = context.getSystemService(PowerManager::class.java)
        val wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WatchChime:ChimeLock")
        wakeLock.acquire(5_000L)

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean(PREF_ENABLED, true)
        val disableWhenLocked = prefs.getBoolean(PREF_DISABLE_LOCKED, true)
        val volume = prefs.getInt(PREF_VOLUME, 10)

        if (disableWhenLocked) {
            val km = context.getSystemService(KeyguardManager::class.java)
            if (km.isDeviceLocked) {
                Log.w("Chime", "Skipping chime. Device is locked.")
                return
            }
        }

        try {
            if (enabled) {
                playDoubleBeep(volume)
            } else {
                Log.w("Chime", "Skipping chime. Disabled in settings.")
            }
        } finally {
            ChimeScheduler.scheduleNext(context)
            wakeLock.release()
        }
    }

    private fun playDoubleBeep(volume: Int) {
        val beepLength = 0.08
        val pauseLength = 0.16
        val totalLength = beepLength + pauseLength + beepLength
        val sampleRate = 44100
        val samples = (sampleRate * totalLength).toInt()
        val frequency = 2048.0
        val buffer = ShortArray(samples)
        val track = AudioTrack(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build(),
            AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build(),
            buffer.size * 2,
            AudioTrack.MODE_STATIC,
            0
        )

        val samplesPerCycle = sampleRate.toDouble() / frequency
        for (i in 0 until samples) {
            val progress = i.mod(samplesPerCycle.toInt()) / samplesPerCycle
            if (i < (beepLength * sampleRate).toInt() || i > ((beepLength + pauseLength) * sampleRate).toInt()) {
                buffer[i] =
                    (((2.0 * progress) - 1.0) * Short.MAX_VALUE * (volume.toFloat() / 100.0)).toInt()
                        .toShort()
            }
        }

        track.write(buffer, 0, buffer.size)
        track.play()
        Thread.sleep((totalLength * 2000).toLong())
        track.release()
    }
}
