package com.practicum.playlistmaker.data


import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.domain.api.HistoryRepository
import com.practicum.playlistmaker.domain.entity.Track




class HistoryRepositoryImpl (
    private val context: Context

) : HistoryRepository{

    companion object{

        const val SEARCH_HISTORY_KEY = "search_history_key"
        const val APP_PREFERENCES = "app_preferences"
    }

    private val sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
    private val gson = Creator.provideGson()



    // About generic type and gson: https://github.com/google/gson/blob/main/UserGuide.md#serializing-and-deserializing-generic-types

    override fun getTracksHistory() : MutableList<Track>{

        val tracksJSON = sharedPreferences.getString(SEARCH_HISTORY_KEY, "[]")

        val type = object : TypeToken<MutableList<Track>>() {}.type

        val tracksList: MutableList<Track> = gson.fromJson(tracksJSON, type)
        return tracksList
    }
    override fun saveTracksHistory(mutableList: MutableList<Track>){
        val gson = Gson()
        val type = object  : TypeToken<MutableList<Track>>() {}.type
        val tracksJSON = gson.toJson(mutableList, type)
        sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, tracksJSON).apply()
    }
    override fun clearHistory(){
        sharedPreferences.edit().clear().apply()
    }
}