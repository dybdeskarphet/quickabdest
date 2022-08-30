package com.ahmetardakavakc.quickabdest

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.ahmetardakavakc.quickabdest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var githubIntent: Intent
    private lateinit var websiteIntent: Intent
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private var darkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/dybdeskarphet"))
        websiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://kavakci.dev"))

        sharedPreferences = getSharedPreferences("Prefs", MODE_PRIVATE)
        prefEditor = sharedPreferences.edit()
        darkMode = sharedPreferences.getBoolean("darkMode", false)

        supportActionBar?.hide()
        checkDarkMode(view)

    }


    fun clickPepe(view: View) {
        binding.pepe.setImageResource(R.drawable.open)
        binding.textView.text = "abdestlendin"
        binding.pepe.isEnabled = false
        mediaPlayer = MediaPlayer.create(this, R.raw.soundeffect)

        try {
            mediaPlayer.start()

            mediaPlayer.setOnCompletionListener {
                binding.pepe.setImageResource(R.drawable.close)
                binding.textView.text = "quickabdest"
                binding.pepe.isEnabled = true
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    fun clickGithub(view: View) {
        startActivity(githubIntent)
    }

    fun clickWebsite(view: View) {
        startActivity(websiteIntent)
    }

    private fun checkDarkMode(view: View){
        if(darkMode) {
            WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = false

            binding.linearLayout.setBackgroundColor(Color.BLACK)
            binding.darkToggle.setImageResource(R.drawable.ic_sun)

            window.navigationBarColor = Color.BLACK
            window.statusBarColor = Color.BLACK
        } else {
            WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = true

            binding.linearLayout.setBackgroundColor(Color.WHITE)
            binding.darkToggle.setImageResource(R.drawable.ic_moon)

            window.navigationBarColor = Color.WHITE
            window.statusBarColor = Color.WHITE
        }
    }

    fun darkModeToggle(view: View) {
        darkMode = !darkMode
        prefEditor.putBoolean("darkMode", darkMode)
        prefEditor.apply()
        checkDarkMode(view)
    }

    private fun releaseMediaPlayer() {
        try {

            if(mediaPlayer.isPlaying){
                mediaPlayer.stop()
            }

            mediaPlayer.release()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopPepe() {
        binding.apply {
            pepe.setImageResource(R.drawable.close)
            textView.text = "quickabdest"
            pepe.isEnabled = true
        }

        try {
            releaseMediaPlayer()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        stopPepe()
    }
}