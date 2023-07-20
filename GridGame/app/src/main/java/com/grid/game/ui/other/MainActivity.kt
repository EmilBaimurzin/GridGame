package com.grid.game.ui.other

import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.grid.game.R
import com.grid.game.domain.MusicController

class MainActivity : AppCompatActivity() , MusicController {
    private val viewModel: ActivityViewModel by viewModels()
    private val sharedPrefs by lazy {
        this.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)
    }
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer.isLooping = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("SEC", mediaPlayer.currentPosition)
    }

    override fun onResume() {
        super.onResume()
        startMusic()
    }

    override fun onPause() {
        super.onPause()
        pauseMusic()
    }

    override fun startMusic() {
        if (sharedPrefs.getBoolean("VOLUME", true)) {
            mediaPlayer.setVolume(50.toFloat(), 50.toFloat())
            mediaPlayer.seekTo(viewModel.currentSec)
            mediaPlayer.start()
        }
    }

    override fun pauseMusic() {
        try {
            viewModel.currentSec = mediaPlayer.currentPosition
            mediaPlayer.pause()
        } catch (_: Throwable) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}