package com.practicum.playlistmaker.domain.api

interface PreferencesRepository {

    fun saveTheme(boolean: Boolean)

    fun getTheme() : Boolean

}