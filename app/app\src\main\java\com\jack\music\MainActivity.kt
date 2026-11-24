package com.jack.music

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class MainActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        player = ExoPlayer.Builder(this).build()

        statusText = TextView(this).apply {
            text = "একটি গান বেছে নিন"
            textSize = 18f
            gravity = Gravity.CENTER
        }

        val chooseButton = Button(this).apply {
            text = "গান বেছে নিন"
            setOnClickListener { chooseAudio() }
        }

        val playButton = Button(this).apply {
            text = "Play"
            setOnClickListener { player.play() }
        }

        val pauseButton = Button(this).apply {
            text = "Pause"
            setOnClickListener { player.pause() }
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            addView(statusText)
            addView(chooseButton)
            addView(playButton)
            addView(pauseButton)
        }

        setContentView(layout)
    }

    private fun chooseAudio() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "audio/*"
        }
        startActivityForResult(intent, 100)
    }

    @Deprecated("Used for the Android document picker")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data ?: return
            player.setMediaItem(MediaItem.fromUri(uri))
            player.prepare()
            player.play()
            statusText.text = "গান চলছে"
        }
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }
}
