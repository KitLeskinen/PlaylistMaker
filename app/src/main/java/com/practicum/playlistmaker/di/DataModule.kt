package com.practicum.playlistmaker.di

import com.google.gson.Gson
import com.practicum.playlistmaker.common.data.network.ITunesApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


val dataModule = module {

    single {
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

}