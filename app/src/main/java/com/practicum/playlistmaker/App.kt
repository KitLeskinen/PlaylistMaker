package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val DARK_THEME_ENABLED = "darkThemeEnabled"

class App : Application(){

    private var darkTheme = false

    override fun onCreate(){
        super.onCreate()
        val sharedPreferences = getSharedPreferences(DARK_THEME_ENABLED, MODE_PRIVATE)
        switchTheme(sharedPreferences.getBoolean(DARK_THEME_ENABLED, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}