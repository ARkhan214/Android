package com.emranhss.stopwatch1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.Settings
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        var player: MediaPlayer? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            Toast.makeText(it, "⏰ Alarm Ringing!", Toast.LENGTH_LONG).show()
            try {
                if (player == null) {
                    player = MediaPlayer.create(it, Settings.System.DEFAULT_ALARM_ALERT_URI)
                    player?.isLooping = true
                    player?.start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(it, "Cannot play alarm sound!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
