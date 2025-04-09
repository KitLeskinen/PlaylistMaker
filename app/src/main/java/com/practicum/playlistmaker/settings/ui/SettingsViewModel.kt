package com.practicum.playlistmaker.settings.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.settings.domain.PreferencesInteractor


class SettingsViewModel( private val application: Application,
    private val preferencesInteractor: PreferencesInteractor
) : ViewModel() {

    private val state = MutableLiveData<SettingsState>()


    fun getState(): LiveData<SettingsState> {
        return state
    }

    fun saveThemePreferences(boolean: Boolean) {
        preferencesInteractor.saveThemePreferences(boolean)


    }

    fun applyTheme(checked: Boolean) {
        (application as App).switchTheme(checked)
    }

    init {
        Log.d("TEST", ": initView")
        state.value = SettingsState.Loading(preferencesInteractor.getThemePreferences())
    }

}