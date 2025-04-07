package com.practicum.playlistmaker.settings.impl


import com.practicum.playlistmaker.settings.domain.PreferencesInteractor
import com.practicum.playlistmaker.settings.domain.PreferencesRepository

class PreferencesInteractorImpl(private val preferencesRepository: PreferencesRepository):
    PreferencesInteractor {

    override fun saveThemePreferences(boolean: Boolean) {
        preferencesRepository.saveTheme(boolean)

    }

    override fun getThemePreferences(): Boolean {
        return preferencesRepository.getTheme()
    }

}