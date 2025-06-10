package com.practicum.playlistmaker.di

import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.common.data.network.ITunesApi
import com.practicum.playlistmaker.medialibrary.data.db.AppDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


val dataModule = module {

    factory {
        Gson()
    }

    single<ITunesApi> {
        val retrofit: Retrofit = get()
        retrofit.create()
    }

    single {
        Retrofit.Builder().baseUrl("https://itunes.apple.com").addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db").build()
    }

}