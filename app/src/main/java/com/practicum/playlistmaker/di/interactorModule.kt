package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.audio_player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.audio_player.impl.AudioPlayerIneractorImpl
import com.practicum.playlistmaker.common.data.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.search.domain.HistoryInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.settings.domain.PreferencesInteractor
import com.practicum.playlistmaker.settings.impl.PreferencesInteractorImpl
import org.koin.dsl.module


val interactorModule = module {
    single<HistoryInteractor> {
        HistoryInteractorImpl(repository = get())
    }

    single<PreferencesInteractor> {
        PreferencesInteractorImpl(preferencesRepository = get())
    }

    single<AudioPlayerInteractor> {
        AudioPlayerIneractorImpl(get())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(repository = get())
    }
}