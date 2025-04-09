package com.practicum.playlistmaker.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.practicum.playlistmaker.audio_player.data.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.audio_player.domain.AudioPlayerRepository
import com.practicum.playlistmaker.common.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.HistoryRepositoryImpl.Companion.APP_PREFERENCES
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.HistoryRepository
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.settings.data.PreferencesRepositoryImpl
import com.practicum.playlistmaker.settings.domain.PreferencesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PreferencesRepository> {
        PreferencesRepositoryImpl(context = get())
    }

    single<HistoryRepository> {
        val context: Context = get()
        HistoryRepositoryImpl(
            sharedPreferences = context.getSharedPreferences(
                APP_PREFERENCES,
                MODE_PRIVATE
            ), get()
        )
    }

    single<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(track = get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(
            RetrofitNetworkClient(
                iTunesApi = get()
            )
        )
    }

}