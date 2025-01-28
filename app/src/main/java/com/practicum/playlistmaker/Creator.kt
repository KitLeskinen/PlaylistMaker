package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.practicum.playlistmaker.data.HistoryRepositoryImpl
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.HistoryInteractor
import com.practicum.playlistmaker.domain.api.HistoryRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
   private fun getTrackRepository(): TracksRepository{
       return TracksRepositoryImpl(RetrofitNetworkClient())
   }

    fun provideTrackInteractor(): TracksInteractor{
        return TracksInteractorImpl(getTrackRepository())
    }

    private fun getHistoryRepository(sharedPreferences: SharedPreferences) : HistoryRepository{
        return HistoryRepositoryImpl(sharedPreferences)
    }

    fun provideHistoryInteractor(sharedPreferences: SharedPreferences): HistoryInteractor{
        return HistoryInteractorImpl(getHistoryRepository(sharedPreferences))
    }

}