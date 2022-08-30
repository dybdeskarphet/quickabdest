package com.ahmetardakavakc.quickabdest

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.ahmetardakavakc.quickabdest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var githubIntent: Intent
    private lateinit var websiteIntent: Intent
    private lateinit var creditsIntent: Intent
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private var darkMode = false
    private var firstStart: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/dybdeskarphet"))
        websiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://kavakci.dev"))
        creditsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://quickabdest.com"))

        sharedPreferences = getSharedPreferences("Prefs", MODE_PRIVATE)
        prefEditor = sharedPreferences.edit()
        darkMode = sharedPreferences.getBoolean("darkMode", false)
        firstStart = sharedPreferences.getBoolean("firstStart", true)

        supportActionBar?.hide()
        checkDarkMode(view)
        checkFirstStart()

    }

    fun clickPepe(view: View) {
        binding.apply {
            pepe.setImageResource(R.drawable.open)
            emphaticText.text = "abdestlendin"
            normalText.visibility = View.GONE
            pepe.isEnabled = false
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.soundeffect)

        try {
            mediaPlayer.start()

            mediaPlayer.setOnCompletionListener {
                binding.apply {
                    pepe.setImageResource(R.drawable.close)
                    emphaticText.text = "tıkla"
                    normalText.visibility = View.VISIBLE
                    pepe.isEnabled = true
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkFirstStart() {
        if (firstStart == true) {
            firstStart = false
            val builder = AlertDialog.Builder(this@MainActivity)
            builder
                .setMessage("Bu uygulamanın quickabdest.com geliştiricisi ile bir bağlantısı yoktur. Orijinal geliştiriciyi desteklemek için ")
                .setPositiveButton("Geliştiriciyi Ziyaret Et"
                ) { _, _ ->
                    startActivity(creditsIntent)
                    prefEditor.apply {
                        putBoolean("firstStart", firstStart!!)
                        apply()
                    }
                }
                .setNegativeButton("Kapat") { _, _ ->
                    prefEditor.apply {
                        putBoolean("firstStart", firstStart!!)
                        apply()
                    }
                }.show()
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

            binding.apply {
                mainLayout.setBackgroundColor(Color.BLACK)
                darkToggle.setImageResource(R.drawable.ic_sun)
                normalText.setTextColor(Color.WHITE)
                appTitle.setTextColor(Color.WHITE)
            }

            window.apply {
                navigationBarColor = Color.BLACK
                statusBarColor = Color.BLACK
            }

        } else {
            WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = true

            binding.apply {
                mainLayout.setBackgroundColor(Color.WHITE)
                darkToggle.setImageResource(R.drawable.ic_moon)
                normalText.setTextColor(Color.BLACK)
                appTitle.setTextColor(Color.BLACK)
            }

            window.apply {
                navigationBarColor = Color.WHITE
                statusBarColor = Color.WHITE
            }
        }
    }

    fun darkModeToggle(view: View) {
        darkMode = !darkMode

        prefEditor.apply {
            putBoolean("darkMode", darkMode)
            apply()
        }

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
            emphaticText.text = "tıkla"
            normalText.visibility = View.VISIBLE
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