package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.audio_player.ui.AudioPlayerViewModel
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(
            historyInteractor = get(),
            trackInteractor = get()
        )
    }
    viewModel {
        SettingsViewModel(
            application = get(),
            preferencesInteractor = get()
        )
    }
    viewModel { (track: Track) ->
        AudioPlayerViewModel(
            track,
            audioPlayerInteractor = get()
        )
    }
}