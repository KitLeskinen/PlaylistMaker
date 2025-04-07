package com.practicum.playlistmaker.settings.domain

interface PreferencesInteractor {

    fun saveThemePreferences(boolean: Boolean)

    fun getThemePreferences() : Boolean

}