package com.practicum.playlistmaker.settings.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.settings.domain.PreferencesInteractor


class SettingsViewModel( private val application: Application,
    private val preferencesInteractor: PreferencesInteractor
) : ViewModel() {

    private val state = MutableLiveData<SettingsState>()


    fun getState(): MutableLiveData<SettingsState> {
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


    companion object {
        fun factory(
            application: Application,
            preferencesInteractor: PreferencesInteractor
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SettingsViewModel(
                        application,
                        preferencesInteractor
                    )
                }
            }
        }
    }
}