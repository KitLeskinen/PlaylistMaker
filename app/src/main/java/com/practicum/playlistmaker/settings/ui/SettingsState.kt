package com.practicum.playlistmaker.settings.ui

sealed interface SettingsState {
    data class Loading(val darkThemeEnabled: Boolean): SettingsState

}