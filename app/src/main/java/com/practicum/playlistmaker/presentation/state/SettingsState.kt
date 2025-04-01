package com.practicum.playlistmaker.presentation.state

sealed interface SettingsState {
    data class Loading(val darkThemeEnabled: Boolean): SettingsState

}