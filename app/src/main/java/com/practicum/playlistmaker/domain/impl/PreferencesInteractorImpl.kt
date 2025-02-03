package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.PreferencesInteractor
import com.practicum.playlistmaker.domain.api.PreferencesRepository

class PreferencesInteractorImpl(private val preferencesRepository: PreferencesRepository): PreferencesInteractor {

    override fun saveThemePreferences(boolean: Boolean) {
        preferencesRepository.saveTheme(boolean)

    }

    override fun getThemePreferences(): Boolean {
        return preferencesRepository.getTheme()
    }

}