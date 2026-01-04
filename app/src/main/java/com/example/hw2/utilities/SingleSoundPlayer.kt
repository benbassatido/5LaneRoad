package com.example.hw2.utilities

import android.content.Context
import android.media.MediaPlayer
import java.util.concurrent.Executors

class SingleSoundPlayer(context: Context) {
    private val context = context.applicationContext
    private val executor = Executors.newSingleThreadExecutor()

    fun playSound(resId: Int) {
        executor.execute {
            val mediaPlayer = MediaPlayer.create(context, resId)
            mediaPlayer.isLooping = false
            mediaPlayer.setVolume(1f, 1f)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp ->
                mp.release()
            }
        }
    }
}
