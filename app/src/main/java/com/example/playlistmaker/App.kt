package com.example.playlistmaker
import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        preferences = getSharedPreferences("settings", MODE_PRIVATE)

        val darkThemeEnabled = preferences.getBoolean("darkTheme", false)
        switchTheme(darkThemeEnabled, save = false)
    }

    fun switchTheme(darkThemeEnabled: Boolean, save: Boolean = true) {
        if (save) {
            preferences.edit().putBoolean("darkTheme", darkThemeEnabled).apply()
        }
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
