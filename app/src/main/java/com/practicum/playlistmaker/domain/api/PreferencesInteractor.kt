package com.practicum.playlistmaker.domain.api

interface PreferencesInteractor {

    fun saveThemePreferences(boolean: Boolean)

    fun getThemePreferences() : Boolean

}