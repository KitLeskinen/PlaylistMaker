package com.practicum.playlistmaker.Creator

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.data.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.data.HistoryRepositoryImpl
import com.practicum.playlistmaker.data.PreferencesRepositoryImpl
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.ITunesApi
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.domain.api.HistoryInteractor
import com.practicum.playlistmaker.domain.api.HistoryRepository
import com.practicum.playlistmaker.domain.api.PreferencesInteractor
import com.practicum.playlistmaker.domain.api.PreferencesRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.domain.impl.AudioPlayerIneractorImpl
import com.practicum.playlistmaker.domain.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.domain.impl.PreferencesInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Creator {


    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }

    private fun getHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(context)
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository(context))
    }

    fun provideGson(): Gson {
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

    fun providePreferencesInteractor(context: Context) : PreferencesInteractor{
        return PreferencesInteractorImpl(getPreferencesRepository(context))
    }

    private fun getAudioPlayerRepository(track: Track) : AudioPlayerRepository{
        return AudioPlayerRepositoryImpl(track)
    }

    fun provideAudioPlayerInteractor(track: Track): AudioPlayerInteractor{
        return AudioPlayerIneractorImpl(getAudioPlayerRepository(track))
    }


}