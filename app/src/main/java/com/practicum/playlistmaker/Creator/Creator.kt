package com.practicum.playlistmaker.Creator

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.audio_player.data.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.audio_player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.audio_player.impl.AudioPlayerIneractorImpl
import com.practicum.playlistmaker.audio_player.domain.AudioPlayerRepository
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.common.data.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.common.data.network.ITunesApi
import com.practicum.playlistmaker.common.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.HistoryInteractor
import com.practicum.playlistmaker.search.domain.HistoryRepository
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.settings.data.PreferencesRepositoryImpl
import com.practicum.playlistmaker.settings.domain.PreferencesInteractor
import com.practicum.playlistmaker.settings.domain.PreferencesRepository
import com.practicum.playlistmaker.settings.impl.PreferencesInteractorImpl

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Creator {


    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(
            getTrackRepository()
        )
    }

    private fun getHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(context, provideGson())
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(
            getHistoryRepository(context)
        )
    }

    private fun provideGson(): Gson {
        return Gson()
    }

    fun provideITunesApi(): ITunesApi {
        val retrofit =
            Retrofit.Builder().baseUrl("https://itunes.apple.com").addConverterFactory(
                GsonConverterFactory.create()
            ).build()
        return retrofit.create<ITunesApi>()
    }

    private fun getPreferencesRepository(context: Context): PreferencesRepository {

        return PreferencesRepositoryImpl(context)

    }

    fun providePreferencesInteractor(context: Context): PreferencesInteractor {
        return PreferencesInteractorImpl(getPreferencesRepository(context))
    }

    private fun getAudioPlayerRepository(track: com.practicum.playlistmaker.common.data.domain.entity.Track): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(track)
    }

    fun provideAudioPlayerInteractor(track: com.practicum.playlistmaker.common.data.domain.entity.Track): AudioPlayerInteractor {
        return AudioPlayerIneractorImpl(
            getAudioPlayerRepository(track)
        )
    }


}