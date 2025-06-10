package com.practicum.playlistmaker.settings.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.settings.domain.PreferencesInteractor


class SettingsViewModel( private val application: Application,
    private val preferencesInteractor: PreferencesInteractor
) : ViewModel() {

    private val state = MutableLiveData<SettingsState>()

    init {
        state.value = SettingsState.Loading(preferencesInteractor.getThemePreferences())
    }

    fun getState(): LiveData<SettingsState> {
        return state
    }

    fun switchTheme() {
        val darkThemeEnabled = !preferencesInteractor.getThemePreferences()
        preferencesInteractor.saveThemePreferences(darkThemeEnabled)
        state.value = SettingsState.Switching
        (application as App).switchTheme(darkThemeEnabled)
    }

}