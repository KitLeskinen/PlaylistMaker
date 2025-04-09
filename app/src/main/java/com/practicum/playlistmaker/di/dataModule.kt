package com.practicum.playlistmaker.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson

import com.practicum.playlistmaker.search.data.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.HistoryRepositoryImpl.Companion.APP_PREFERENCES
import com.practicum.playlistmaker.search.domain.HistoryInteractor
import com.practicum.playlistmaker.search.domain.HistoryRepository
import com.practicum.playlistmaker.search.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.search.ui.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val dataModule = module{
    single<HistoryRepository>{
        val context: Context = get()
        HistoryRepositoryImpl(sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE), get())
    }
    single{
        Gson()
    }

    single<HistoryInteractor>{
        HistoryInteractorImpl(get())
    }




}

val viewModelModule = module {
    viewModel{
        SearchViewModel(historyInteractor = get())
    }
}