package com.practicum.playlistmaker.settings.data

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.practicum.playlistmaker.settings.domain.PreferencesRepository

class PreferencesRepositoryImpl(private val context: Context):
    PreferencesRepository {

    companion object{
        const val DARK_THEME_ENABLED = "darkThemeEnabled"
    }

    override fun saveTheme(boolean: Boolean) {

        val sharedPreferences = context.getSharedPreferences(com.practicum.playlistmaker.DARK_THEME_ENABLED, MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(com.practicum.playlistmaker.DARK_THEME_ENABLED, boolean).apply()

    }

    override fun getTheme() : Boolean{

        return context.getSharedPreferences(DARK_THEME_ENABLED, MODE_PRIVATE).getBoolean(
            DARK_THEME_ENABLED, false)

    }
}