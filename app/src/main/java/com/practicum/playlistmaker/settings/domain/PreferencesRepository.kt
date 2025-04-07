package com.practicum.playlistmaker.settings.domain

interface PreferencesRepository {

    fun saveTheme(boolean: Boolean)

    fun getTheme() : Boolean

}